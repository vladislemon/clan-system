package org.example.clan.transaction.gold;

import org.example.clan.clan.ClanService;
import org.example.clan.task.TaskService;
import org.example.clan.transaction.TransactionSubjectType;
import org.example.clan.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GoldTransactionServiceImpl implements GoldTransactionService {
    private static final int NEED_FLUSH_PENDING_TRANSACTIONS = 100;
    private static final int FLUSH_INTERVAL = 200;
    private static final int MAX_PENDING_TRANSACTIONS = 10000;
    private final GoldTransactionRepository goldTransactionRepository;
    private final UserService userService;
    private final ClanService clanService;
    private final TaskService taskService;
    private final AtomicInteger pendingTransactionsCount = new AtomicInteger();
    private final ReadWriteLock flushLock = new ReentrantReadWriteLock();
    private final Condition maxPendingTransactionsCondition = flushLock.writeLock().newCondition();
    private CountDownLatch flushCountDownLatch = new CountDownLatch(NEED_FLUSH_PENDING_TRANSACTIONS);
    private Queue<GoldTransaction> pendingTransactions = new ConcurrentLinkedQueue<>();
    private Map<Long, AtomicInteger> userGoldCache = new ConcurrentHashMap<>();
    private Map<Long, AtomicInteger> clanGoldCache = new ConcurrentHashMap<>();

    public GoldTransactionServiceImpl(GoldTransactionRepository goldTransactionRepository,
                                      UserService userService,
                                      ClanService clanService,
                                      TaskService taskService) {
        this.goldTransactionRepository = goldTransactionRepository;
        this.userService = userService;
        this.clanService = clanService;
        this.taskService = taskService;
        Thread flushingThread = new Thread(this::doPeriodicFlush, "Gold-Transactions-Flushing-Thread");
        flushingThread.setDaemon(true);
        flushingThread.start();
    }

    @Override
    public void sendGoldFromUserToClan(long userId, long clanId, int amount, String description) throws InterruptedException {
        waitIfQueueFull();
        flushLock.readLock().lock();
        try {
            AtomicInteger userGold = getUserGold(userId);
            AtomicInteger clanGold = getClanGold(clanId);
            int lastKnownUserGold;
            do {
                lastKnownUserGold = userGold.get();
                if (lastKnownUserGold < amount) {
                    throw new IllegalStateException(String.format("User with id '%d' has not enough gold", userId));
                }
            } while (!userGold.compareAndSet(lastKnownUserGold, lastKnownUserGold - amount));
            clanGold.addAndGet(amount);
            addPendingTransaction(GoldTransaction.userToClan(userId, clanId, amount, description));
        } finally {
            flushLock.readLock().unlock();
        }
    }

    @Override
    public void sendGoldFromTaskToClan(long taskId, long clanId, String description) throws InterruptedException {
        waitIfQueueFull();
        flushLock.readLock().lock();
        try {
            int taskGoldReward = taskService.getTask(taskId).getGoldReward();
            AtomicInteger clanGold = getClanGold(clanId);
            clanGold.addAndGet(taskGoldReward);
            addPendingTransaction(GoldTransaction.taskToClan(taskId, clanId, taskGoldReward, description));
        } finally {
            flushLock.readLock().unlock();
        }
    }

    @Override
    public GoldTransaction getGoldTransaction(long goldTransactionId) {
        for (GoldTransaction transaction : pendingTransactions) {
            if (transaction.getId() == goldTransactionId) {
                return transaction;
            }
        }
        return goldTransactionRepository.getGoldTransaction(goldTransactionId);
    }

    @Override
    public List<GoldTransaction> getGoldTransactionsByUserId(long userId) {
        List<GoldTransaction> transactions = new ArrayList<>(goldTransactionRepository.getGoldTransactionsByUserId(userId));
        for (GoldTransaction transaction : pendingTransactions) {
            if (isTransactionCorresponding(transaction, TransactionSubjectType.USER, userId)) {
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    @Override
    public List<GoldTransaction> getGoldTransactionsByClanId(long clanId) {
        List<GoldTransaction> transactions = new ArrayList<>(goldTransactionRepository.getGoldTransactionsByClanId(clanId));
        for (GoldTransaction transaction : pendingTransactions) {
            if (isTransactionCorresponding(transaction, TransactionSubjectType.CLAN, clanId)) {
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    @Override
    public List<GoldTransaction> getGoldTransactionsByTaskId(long taskId) {
        List<GoldTransaction> transactions = new ArrayList<>(goldTransactionRepository.getGoldTransactionsByTaskId(taskId));
        for (GoldTransaction transaction : pendingTransactions) {
            if (isTransactionCorresponding(transaction, TransactionSubjectType.TASK, taskId)) {
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    private AtomicInteger getUserGold(long userId) {
        AtomicInteger userGold = userGoldCache.get(userId);
        if (userGold == null) {
            userGold = new AtomicInteger(userService.getUser(userId).getGold());
            AtomicInteger alreadyPresentGold = userGoldCache.putIfAbsent(userId, userGold);
            if (alreadyPresentGold != null) {
                return alreadyPresentGold;
            }
        }
        return userGold;
    }

    private AtomicInteger getClanGold(long clanId) {
        AtomicInteger clanGold = clanGoldCache.get(clanId);
        if (clanGold == null) {
            clanGold = new AtomicInteger(clanService.getClan(clanId).getGold());
            AtomicInteger alreadyPresentGold = clanGoldCache.putIfAbsent(clanId, clanGold);
            if (alreadyPresentGold != null) {
                return alreadyPresentGold;
            }
        }
        return clanGold;
    }

    private void waitIfQueueFull() throws InterruptedException {
        while (pendingTransactionsCount.get() >= MAX_PENDING_TRANSACTIONS) {
            flushLock.writeLock().lock();
            try {
                maxPendingTransactionsCondition.await();
            } finally {
                flushLock.writeLock().unlock();
            }
        }
    }

    private void addPendingTransaction(GoldTransaction transaction) {
        pendingTransactions.add(transaction);
        flushCountDownLatch.countDown();
    }

    private void doPeriodicFlush() {
        try {
            for (; ; ) {
                //noinspection ResultOfMethodCallIgnored
                flushCountDownLatch.await(FLUSH_INTERVAL, TimeUnit.MILLISECONDS);
                flushLock.writeLock().lock();
                try {
                    flush();
                } finally {
                    flushCountDownLatch = new CountDownLatch(NEED_FLUSH_PENDING_TRANSACTIONS);
                    maxPendingTransactionsCondition.signalAll();
                    flushLock.writeLock().unlock();
                }
            }
        } catch (InterruptedException e) {
            // UNSAVED TRANSACTIONS WILL BE LOST
        }
    }

    private void flush() {
        //TODO next 5 lines of code must execute in single DB transaction
        for (GoldTransaction pendingTransaction : pendingTransactions) {
            goldTransactionRepository.createGoldTransaction(pendingTransaction);
        }
        userGoldCache.forEach((userId, gold) -> userService.setUserGold(userId, gold.get()));
        clanGoldCache.forEach((clanId, gold) -> clanService.setClanGold(clanId, gold.get()));
        pendingTransactions = new ConcurrentLinkedQueue<>();
        userGoldCache = new ConcurrentHashMap<>();
        clanGoldCache = new ConcurrentHashMap<>();
    }

    private static boolean isTransactionCorresponding(GoldTransaction transaction, TransactionSubjectType subjectType, long subjectId) {
        if (transaction.getSourceType() == subjectType && transaction.getSourceId() == subjectId) {
            return true;
        }
        return transaction.getRecipientType() == subjectType && transaction.getRecipientId() == subjectId;
    }
}

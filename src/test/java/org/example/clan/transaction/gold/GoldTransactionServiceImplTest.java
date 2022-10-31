package org.example.clan.transaction.gold;

import org.example.clan.DbInitializer;
import org.example.clan.clan.*;
import org.example.clan.task.TaskRepository;
import org.example.clan.task.TaskRepositoryImpl;
import org.example.clan.user.*;
import org.example.clan.util.ConnectionManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoldTransactionServiceImplTest {

    static ConnectionManager connectionManager;
    static GoldTransactionRepository goldTransactionRepository;
    UserRepository userRepository = new UserRepositoryImpl(connectionManager);
    ClanRepository clanRepository = new ClanRepositoryImpl(connectionManager);
    TaskRepository taskRepository = new TaskRepositoryImpl(connectionManager);
    GoldTransactionServiceImpl goldTransactionService = new GoldTransactionServiceImpl(
            userRepository,
            clanRepository,
            taskRepository,
            goldTransactionRepository
    );
    UserService userService = new UserServiceImpl(userRepository, goldTransactionService);
    ClanService clanService = new ClanServiceImpl(clanRepository, goldTransactionService);

    @BeforeAll
    static void beforeAll() throws SQLException {
        connectionManager = new ConnectionManager("jdbc:h2:mem:", "", "");
        new DbInitializer(connectionManager).createTables();
        goldTransactionRepository = spy(new GoldTransactionRepositoryImpl(connectionManager));
    }

    @BeforeEach
    void setUp() {
        reset(goldTransactionRepository);
    }

    @Test
    void sendGoldFromUserToClan_notEnoughGold() throws InterruptedException {
        String userName = "user_with_not_enough_gold";
        String clanName = "clan_that_will_be_with_no_gold";
        userService.createUser(userName, 0);
        clanService.createClan(clanName, 0);
        User user = userService.findUserByName(userName).orElseThrow(() -> new IllegalStateException("User not found"));
        Clan clan = clanService.findClanByName(clanName).orElseThrow(() -> new IllegalStateException("Clan not found"));
        assertThrows(IllegalStateException.class,
                () -> goldTransactionService.sendGoldFromUserToClan(user.getId(), clan.getId(), 1, "Sample text"),
                String.format("User with id '%d' has not enough gold", user.getId()));
    }

    @Test
    void sendGoldFromUserToClan_success() throws InterruptedException {
        String userName = "user_with_one_piece_of_gold";
        String clanName = "happy_clan_that_will_has_gold";
        int amount = 999;
        String description = "Sample text";
        userService.createUser(userName, amount);
        clanService.createClan(clanName, 0);
        User user = userService.findUserByName(userName).orElseThrow(() -> new IllegalStateException("User not found"));
        Clan clan = clanService.findClanByName(clanName).orElseThrow(() -> new IllegalStateException("Clan not found"));
        goldTransactionService.sendGoldFromUserToClan(user.getId(), clan.getId(), amount, description);
        GoldTransaction goldTransaction = goldTransactionService.getGoldTransactionsByUserId(user.getId()).get(1);
        assertEquals(amount, goldTransaction.getAmount());
        assertEquals(description, goldTransaction.getDescription());
    }

    @Test
    void sendGoldFromUserToClan_100threads() throws InterruptedException {
        doNothing().when(goldTransactionRepository).createGoldTransaction(any(GoldTransaction.class));
        String userName = "rich_user";
        int initialUserGold = 500000;
        String clanName = "clan_that_will_be_rich";
        int threadsCount = 100;
        userService.createUser(userName, initialUserGold);
        clanService.createClan(clanName, 0);
        User user = userService.findUserByName(userName).orElseThrow(() -> new IllegalStateException("User not found"));
        Clan clan = clanService.findClanByName(clanName).orElseThrow(() -> new IllegalStateException("Clan not found"));
        ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);
        CountDownLatch countDownLatch = new CountDownLatch(initialUserGold);
        for (int i = 0; i < threadsCount; i++) {
            executorService.submit(() -> {
                int iteration = 0;
                while (countDownLatch.getCount() > 0) {
                    if (Thread.interrupted()) {
                        return;
                    }
                    String description = Thread.currentThread().getName() + ", Iteration: " + iteration;
                    try {
                        goldTransactionService.sendGoldFromUserToClan(user.getId(), clan.getId(), 1, description);
                        countDownLatch.countDown();
                    } catch (Exception e) {
                        //
                    }
                    iteration++;
                }
            });
        }
        boolean isAllGoldSent = countDownLatch.await(10, TimeUnit.SECONDS);
        executorService.shutdownNow();
        assertTrue(isAllGoldSent);
    }

    @Test
    void sendGoldFromUserToClan_checkCorrectness() throws InterruptedException {
        String userName = "punctual_user";
        int initialUserGold = 10000;
        String clanName = "clan_that_will_had_exactly_10000";
        int threadsCount = 100;
        userService.createUser(userName, initialUserGold);
        clanService.createClan(clanName, 0);
        User user = userService.findUserByName(userName).orElseThrow(() -> new IllegalStateException("User not found"));
        Clan clan = clanService.findClanByName(clanName).orElseThrow(() -> new IllegalStateException("Clan not found"));
        ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);
        CountDownLatch countDownLatch = new CountDownLatch(initialUserGold);
        for (int i = 0; i < threadsCount; i++) {
            executorService.submit(() -> {
                int iteration = 0;
                while (countDownLatch.getCount() > 0) {
                    if (Thread.interrupted()) {
                        return;
                    }
                    String description = Thread.currentThread().getName() + ", Iteration: " + iteration;
                    try {
                        goldTransactionService.sendGoldFromUserToClan(user.getId(), clan.getId(), 1, description);
                        countDownLatch.countDown();
                    } catch (Exception e) {
                        //
                    }
                    iteration++;
                }
            });
        }
        boolean isAllGoldSent = countDownLatch.await(10, TimeUnit.SECONDS);
        executorService.shutdownNow();
        assertTrue(isAllGoldSent);
        Thread.sleep(1000);
        assertEquals(0, userService.getUser(user.getId()).getGold());
        assertEquals(initialUserGold, clanService.getClan(clan.getId()).getGold());
        int goldFromTransactions = goldTransactionService.getGoldTransactionsByClanId(clan.getId())
                .stream()
                .mapToInt(GoldTransaction::getAmount)
                .sum();
        assertEquals(initialUserGold, goldFromTransactions);
    }
}
package org.example.clan.transaction.gold;

import java.util.List;

public interface GoldTransactionService {

    void sendGoldFromUserToClan(long userId, long clanId, int amount, String description) throws InterruptedException;

    void sendGoldFromTaskToClan(long taskId, long clanId, String description) throws InterruptedException;

    void addGoldToUser(long userId, int amount, String description) throws InterruptedException;

    void addGoldToClan(long clanId, int amount, String description) throws InterruptedException;

    GoldTransaction getGoldTransaction(long goldTransactionId);

    List<GoldTransaction> getGoldTransactionsByUserId(long userId);

    List<GoldTransaction> getGoldTransactionsByClanId(long clanId);

    List<GoldTransaction> getGoldTransactionsByTaskId(long taskId);

}

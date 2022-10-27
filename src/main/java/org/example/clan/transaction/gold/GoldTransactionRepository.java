package org.example.clan.transaction.gold;

import java.util.List;

public interface GoldTransactionRepository {

    GoldTransaction getGoldTransaction(long goldTransactionId);

    List<GoldTransaction> getGoldTransactionsByUserId(long userId);

    List<GoldTransaction> getGoldTransactionsByClanId(long clanId);

    List<GoldTransaction> getGoldTransactionsByTaskId(long taskId);

    void createGoldTransaction(GoldTransaction transaction);

}

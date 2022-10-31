package org.example.clan.transaction.gold;

import org.example.clan.transaction.TransactionSubjectType;
import org.example.clan.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GoldTransactionRepositoryImpl implements GoldTransactionRepository {
    private final ConnectionManager connectionManager;

    public GoldTransactionRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public GoldTransaction getGoldTransaction(long goldTransactionId) {
        String sql = "SELECT * FROM gold_transactions WHERE id = ?";
        try {
            return connectionManager.executeQuery(sql, this::extractGoldTransaction, goldTransactionId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GoldTransaction> getGoldTransactionsByUserId(long userId) {
        return getGoldTransactionBySourceOrRecipient(TransactionSubjectType.USER, userId, TransactionSubjectType.USER, userId);
    }

    @Override
    public List<GoldTransaction> getGoldTransactionsByClanId(long clanId) {
        return getGoldTransactionBySourceOrRecipient(TransactionSubjectType.CLAN, clanId, TransactionSubjectType.CLAN, clanId);
    }

    @Override
    public List<GoldTransaction> getGoldTransactionsByTaskId(long taskId) {
        return getGoldTransactionBySourceOrRecipient(TransactionSubjectType.TASK, taskId, TransactionSubjectType.TASK, taskId);
    }

    @Override
    public List<GoldTransaction> getAllGoldTransactions() {
        String sql = "SELECT * FROM gold_transactions";
        try {
            return connectionManager.executeQuery(sql, this::extractGoldTransactions);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createGoldTransaction(GoldTransaction transaction) {
        String sql = "INSERT INTO gold_transactions (source_type, source_id, recipient_type, recipient_id, amount, description, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            connectionManager.executeUpdate(sql, transaction.getSourceType().name(), transaction.getSourceId(),
                    transaction.getRecipientType().name(), transaction.getRecipientId(), transaction.getAmount(),
                    transaction.getDescription(), transaction.getCreatedAt());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<GoldTransaction> getGoldTransactionBySourceOrRecipient(TransactionSubjectType sourceType,
                                                                        long sourceId,
                                                                        TransactionSubjectType recipientType,
                                                                        long recipientId) {
        String sql = "SELECT * FROM gold_transactions " +
                "WHERE (source_type = ? AND source_id = ?) OR (recipient_type = ? AND recipient_id = ?)";
        try {
            return connectionManager.executeQuery(sql, this::extractGoldTransactions,
                    sourceType.name(), sourceId, recipientType.name(), recipientId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private GoldTransaction mapToGoldTransaction(ResultSet resultSet) throws SQLException {
        return new GoldTransaction(
                resultSet.getLong("id"),
                TransactionSubjectType.valueOf(resultSet.getString("source_type")),
                resultSet.getLong("source_id"),
                TransactionSubjectType.valueOf(resultSet.getString("recipient_type")),
                resultSet.getLong("recipient_id"),
                resultSet.getInt("amount"),
                resultSet.getString("description"),
                resultSet.getTimestamp("created_at").toInstant()
        );
    }

    private GoldTransaction extractGoldTransaction(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return mapToGoldTransaction(resultSet);
        }
        throw new IllegalArgumentException("Gold transaction not found");
    }

    private List<GoldTransaction> extractGoldTransactions(ResultSet resultSet) throws SQLException {
        List<GoldTransaction> goldTransactions = new ArrayList<>();
        while (resultSet.next()) {
            goldTransactions.add(mapToGoldTransaction(resultSet));
        }
        return goldTransactions;
    }
}

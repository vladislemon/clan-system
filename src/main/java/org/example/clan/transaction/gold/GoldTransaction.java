package org.example.clan.transaction.gold;

import org.example.clan.transaction.TransactionStatus;
import org.example.clan.transaction.TransactionSubjectType;

public class GoldTransaction {
    private final TransactionSubjectType sourceType;
    private final long sourceId;
    private final TransactionSubjectType recipientType;
    private final long recipientId;
    private final int amount;
    private final String description;
    private TransactionStatus status;

    private GoldTransaction(TransactionSubjectType sourceType,
                            long sourceId,
                            TransactionSubjectType recipientType,
                            long recipientId,
                            int amount,
                            String description,
                            TransactionStatus status) {
        this.sourceType = sourceType;
        this.sourceId = sourceId;
        this.recipientType = recipientType;
        this.recipientId = recipientId;
        this.amount = amount;
        this.description = description;
        this.status = status;
    }

    public static GoldTransaction userToClan(long senderUserId, long recipientClanId, int amount, String description) {
        return new GoldTransaction(
                TransactionSubjectType.USER,
                senderUserId,
                TransactionSubjectType.CLAN,
                recipientClanId,
                amount,
                description,
                TransactionStatus.PENDING
        );
    }

    public static GoldTransaction taskToClan(long sourceTaskId, long recipientClanId, int amount, String description) {
        return new GoldTransaction(
                TransactionSubjectType.TASK,
                sourceTaskId,
                TransactionSubjectType.CLAN,
                recipientClanId,
                amount,
                description,
                TransactionStatus.PENDING
        );
    }

    public TransactionSubjectType getSourceType() {
        return sourceType;
    }

    public long getSourceId() {
        return sourceId;
    }

    public TransactionSubjectType getRecipientType() {
        return recipientType;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public int getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}

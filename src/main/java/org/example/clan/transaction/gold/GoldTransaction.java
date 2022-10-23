package org.example.clan.transaction.gold;

import org.example.clan.transaction.TransactionStatus;
import org.example.clan.transaction.TransactionSubjectType;

import java.util.Objects;

public class GoldTransaction {
    private final long id;
    private final TransactionSubjectType sourceType;
    private final long sourceId;
    private final TransactionSubjectType recipientType;
    private final long recipientId;
    private final int amount;
    private final String description;
    private TransactionStatus status;

    private GoldTransaction(long id,
                            TransactionSubjectType sourceType,
                            long sourceId,
                            TransactionSubjectType recipientType,
                            long recipientId,
                            int amount,
                            String description,
                            TransactionStatus status) {
        this.id = id;
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
                0,
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
                0,
                TransactionSubjectType.TASK,
                sourceTaskId,
                TransactionSubjectType.CLAN,
                recipientClanId,
                amount,
                description,
                TransactionStatus.PENDING
        );
    }

    public long getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoldTransaction that = (GoldTransaction) o;
        return id == that.id && sourceId == that.sourceId && recipientId == that.recipientId && amount == that.amount
                && sourceType == that.sourceType && recipientType == that.recipientType
                && Objects.equals(description, that.description) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

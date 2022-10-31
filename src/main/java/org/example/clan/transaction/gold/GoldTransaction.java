package org.example.clan.transaction.gold;

import org.example.clan.transaction.TransactionSubjectType;

import java.time.Instant;
import java.util.Objects;

public class GoldTransaction {
    private final long id;
    private final TransactionSubjectType sourceType;
    private final long sourceId;
    private final TransactionSubjectType recipientType;
    private final long recipientId;
    private final int amount;
    private final String description;
    private final Instant createdAt;

    public GoldTransaction(long id,
                           TransactionSubjectType sourceType,
                           long sourceId,
                           TransactionSubjectType recipientType,
                           long recipientId,
                           int amount,
                           String description,
                           Instant createdAt) {
        this.id = id;
        this.sourceType = sourceType;
        this.sourceId = sourceId;
        this.recipientType = recipientType;
        this.recipientId = recipientId;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
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
                Instant.now());
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
                Instant.now());
    }

    public static GoldTransaction systemToUser(long recipientUserId, int amount, String description) {
        return new GoldTransaction(
                0,
                TransactionSubjectType.SYSTEM,
                0,
                TransactionSubjectType.USER,
                recipientUserId,
                amount,
                description,
                Instant.now());
    }

    public static GoldTransaction systemToClan(long recipientClanId, int amount, String description) {
        return new GoldTransaction(
                0,
                TransactionSubjectType.SYSTEM,
                0,
                TransactionSubjectType.CLAN,
                recipientClanId,
                amount,
                description,
                Instant.now());
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoldTransaction that = (GoldTransaction) o;
        return id == that.id && sourceId == that.sourceId && recipientId == that.recipientId && amount == that.amount
                && sourceType == that.sourceType && recipientType == that.recipientType
                && Objects.equals(description, that.description) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

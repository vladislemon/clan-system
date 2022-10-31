package org.example.clan.transaction.gold;

public class GoldTransactionDto {
    private String sourceType;
    private long sourceId;
    private String recipientType;
    private long recipientId;
    private int amount;
    private String description;
    private String createdAt;

    public GoldTransactionDto(String sourceType,
                              long sourceId,
                              String recipientType,
                              long recipientId,
                              int amount,
                              String description,
                              String createdAt) {
        this.sourceType = sourceType;
        this.sourceId = sourceId;
        this.recipientType = recipientType;
        this.recipientId = recipientId;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public long getSourceId() {
        return sourceId;
    }

    public void setSourceId(long sourceId) {
        this.sourceId = sourceId;
    }

    public String getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(String recipientType) {
        this.recipientType = recipientType;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(long recipientId) {
        this.recipientId = recipientId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

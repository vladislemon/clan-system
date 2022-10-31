package org.example.clan.transaction.gold;

import org.example.clan.transaction.TransactionSubjectType;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GoldTransactionMapper {
    public GoldTransactionDto toDto(GoldTransaction goldTransaction) {
        return new GoldTransactionDto(
                goldTransaction.getSourceType().name(),
                goldTransaction.getSourceId(),
                goldTransaction.getRecipientType().name(),
                goldTransaction.getRecipientId(),
                goldTransaction.getAmount(),
                goldTransaction.getDescription(),
                goldTransaction.getCreatedAt().toString()
        );
    }

    public List<GoldTransactionDto> toDtos(Collection<GoldTransaction> goldTransactions) {
        return goldTransactions.stream().map(this::toDto).collect(Collectors.toList());
    }

    public GoldTransaction fromDto(GoldTransactionDto goldTransactionDto) {
        return new GoldTransaction(
                0,
                TransactionSubjectType.valueOf(goldTransactionDto.getSourceType()),
                goldTransactionDto.getSourceId(),
                TransactionSubjectType.valueOf(goldTransactionDto.getRecipientType()),
                goldTransactionDto.getRecipientId(),
                goldTransactionDto.getAmount(),
                goldTransactionDto.getDescription(),
                null
        );
    }
}

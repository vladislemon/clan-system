package org.example.clan.transaction.gold;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

public class GoldTransactionController {
    private final GoldTransactionService goldTransactionService;
    private final GoldTransactionMapper goldTransactionMapper;
    private final ObjectMapper objectMapper;

    public GoldTransactionController(GoldTransactionService goldTransactionService,
                                     GoldTransactionMapper goldTransactionMapper,
                                     ObjectMapper objectMapper) {
        this.goldTransactionService = goldTransactionService;
        this.goldTransactionMapper = goldTransactionMapper;
        this.objectMapper = objectMapper;
        Spark.post("/api/transactions/gold", "application/json", this::createGoldTransaction);
        Spark.get("/api/transactions/gold", this::getGoldTransactions);
    }

    public Object createGoldTransaction(Request request, Response response) throws IOException, InterruptedException {
        GoldTransactionDto goldTransactionDto = objectMapper.readValue(request.bodyAsBytes(), GoldTransactionDto.class);
        GoldTransaction goldTransaction = goldTransactionMapper.fromDto(goldTransactionDto);
        goldTransactionService.createTransaction(goldTransaction);
        return "";
    }

    public Object getGoldTransactions(Request request, Response response) throws JsonProcessingException {
        Optional<Long> userIdParam = getLongParameter(request, "userId");
        if (userIdParam.isPresent()) {
            return getGoldTransactions(() -> goldTransactionService.getGoldTransactionsByUserId(userIdParam.get()));
        }
        Optional<Long> clanIdParam = getLongParameter(request, "clanId");
        if (clanIdParam.isPresent()) {
            return getGoldTransactions(() -> goldTransactionService.getGoldTransactionsByClanId(clanIdParam.get()));
        }
        Optional<Long> taskIdParam = getLongParameter(request, "taskId");
        if (taskIdParam.isPresent()) {
            return getGoldTransactions(() -> goldTransactionService.getGoldTransactionsByTaskId(taskIdParam.get()));
        }
        return getGoldTransactions(goldTransactionService::getAllGoldTransactions);
    }

    private Object getGoldTransactions(Supplier<Collection<GoldTransaction>> goldTransactionsSupplier) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(goldTransactionMapper.toDtos(goldTransactionsSupplier.get()));
    }

    private Optional<Long> getLongParameter(Request request, String parameterName) {
        try {
            return Optional.of(Long.parseLong(request.queryParams(parameterName)));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}

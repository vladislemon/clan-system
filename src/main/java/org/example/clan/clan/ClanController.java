package org.example.clan.clan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.io.IOException;
import java.util.List;

public class ClanController {
    private final ClanService clanService;
    private final ClanMapper clanMapper;
    private final ObjectMapper objectMapper;

    public ClanController(ClanService clanService, ClanMapper clanMapper, ObjectMapper objectMapper) {
        this.clanService = clanService;
        this.clanMapper = clanMapper;
        this.objectMapper = objectMapper;
        Spark.post("/api/clans", "application/json", this::createClan);
        Spark.get("/api/clans", this::getAllClans);
    }

    public Object createClan(Request request, Response response) throws IOException {
        ClanDto clanDto = objectMapper.readValue(request.bodyAsBytes(), ClanDto.class);
        clanService.createClan(clanDto.getName(), clanDto.getGold());
        return "";
    }

    public Object getAllClans(Request request, Response response) throws JsonProcessingException {
        List<ClanDto> clanDtos = clanMapper.toDtos(clanService.getAllClans());
        clanDtos.add(new ClanDto("ololo", 5));
        return objectMapper.writeValueAsBytes(clanDtos);
    }
}

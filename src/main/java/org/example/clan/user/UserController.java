package org.example.clan.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.io.IOException;
import java.util.List;

public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;

    public UserController(UserService userService, UserMapper userMapper, ObjectMapper objectMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.objectMapper = objectMapper;
        Spark.post("/api/users", "application/json", this::createUser);
        Spark.get("/api/users", this::getAllUsers);
    }

    public Object createUser(Request request, Response response) throws IOException {
        UserDto userDto = objectMapper.readValue(request.bodyAsBytes(), UserDto.class);
        userService.createUser(userDto.getName(), userDto.getGold());
        return "";
    }

    public Object getAllUsers(Request request, Response response) throws JsonProcessingException {
        List<UserDto> userDtos = userMapper.toDtos(userService.getAllUsers());
        userDtos.add(new UserDto("ololo", 5));
        return objectMapper.writeValueAsBytes(userDtos);
    }
}

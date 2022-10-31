package org.example.clan.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.io.IOException;
import java.util.List;

public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final ObjectMapper objectMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper, ObjectMapper objectMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
        this.objectMapper = objectMapper;
        Spark.post("/api/tasks", "application/json", this::createTask);
        Spark.get("/api/tasks", this::getAllTasks);
    }

    public Object createTask(Request request, Response response) throws IOException {
        TaskDto taskDto = objectMapper.readValue(request.bodyAsBytes(), TaskDto.class);
        taskService.createTask(taskDto.getGoldReward());
        return "";
    }

    public Object getAllTasks(Request request, Response response) throws JsonProcessingException {
        List<TaskDto> taskDtos = taskMapper.toDtos(taskService.getAllTasks());
        return objectMapper.writeValueAsBytes(taskDtos);
    }
}

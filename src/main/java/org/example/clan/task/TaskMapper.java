package org.example.clan.task;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TaskMapper {
    public TaskDto toDto(Task task) {
        return new TaskDto(task.getGoldReward());
    }

    public List<TaskDto> toDtos(Collection<Task> tasks) {
        return tasks.stream().map(this::toDto).collect(Collectors.toList());
    }
}

package org.example.clan.task;

import java.util.List;

public interface TaskRepository {

    Task getTask(long taskId);

    List<Task> getAllTasks();

    void createTask(Task task);

}

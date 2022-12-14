package org.example.clan.task;

import java.util.List;

public interface TaskService {

    Task getTask(long taskId);

    List<Task> getAllTasks();

    void createTask(int goldReward);

}

package org.example.clan.task;

import java.util.List;

public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task getTask(long taskId) {
        return taskRepository.getTask(taskId);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.getAllTasks();
    }

    @Override
    public void createTask(int goldReward) {
        taskRepository.createTask(new Task(0, goldReward));
    }
}

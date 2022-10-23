package org.example.clan.task;

import org.example.clan.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskRepositoryImpl implements TaskRepository {
    private final ConnectionManager connectionManager;

    public TaskRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Task getTask(long taskId) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        try {
            return connectionManager.executeQuery(sql, this::extractTask, taskId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Task> getAllTasks() {
        String sql = "SELECT * FROM tasks";
        try {
            return connectionManager.executeQuery(sql, this::extractTasks);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createTask(Task task) {
        String sql = "INSERT INTO tasks (gold_reward) VALUES (?)";
        try {
            connectionManager.executeUpdate(sql, task.getGoldReward());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Task mapToTask(ResultSet resultSet) throws SQLException {
        return new Task(
                resultSet.getLong("id"),
                resultSet.getInt("gold_reward")
        );
    }

    private Task extractTask(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return mapToTask(resultSet);
        }
        throw new IllegalArgumentException("Task not found");
    }

    private List<Task> extractTasks(ResultSet resultSet) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        while (resultSet.next()) {
            tasks.add(mapToTask(resultSet));
        }
        return tasks;
    }
}

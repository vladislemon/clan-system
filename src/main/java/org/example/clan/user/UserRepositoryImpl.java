package org.example.clan.user;

import org.example.clan.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private final ConnectionManager connectionManager;

    public UserRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public User getUser(long userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            return connectionManager.executeQuery(sql, this::extractUser, userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        try {
            return connectionManager.executeQuery(sql, this::extractUsers);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUser(User user) {
        String sql = "INSERT INTO users (name, gold) VALUES (?, ?)";
        try {
            connectionManager.executeUpdate(sql, user.getName(), user.getGold());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User mapToUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getInt("gold")
        );
    }

    private User extractUser(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return mapToUser(resultSet);
        }
        throw new IllegalArgumentException("User not found");
    }

    private List<User> extractUsers(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            users.add(mapToUser(resultSet));
        }
        return users;
    }
}

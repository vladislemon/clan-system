package org.example.clan.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {
    private final Connection connection;

    public ConnectionManager(String dbUrl, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(dbUrl, user, password);
    }

    public <T> T executeQuery(String sql, ResultSetMapper<T> resultSetMapper, Object... args) throws SQLException {
        synchronized (connection) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                setArguments(statement, args);
                return resultSetMapper.map(statement.executeQuery());
            }
        }
    }

    public int executeUpdate(String sql, Object... args) throws SQLException {
        synchronized (connection) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                setArguments(statement, args);
                return statement.executeUpdate();
            }
        }
    }

    public boolean execute(String sql) throws SQLException {
        synchronized (connection) {
            try (Statement statement = connection.createStatement()) {
                return statement.execute(sql);
            }
        }
    }

    private static void setArguments(PreparedStatement statement, Object... args) throws SQLException {
        if (args == null) {
            return;
        }
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            int parameterIndex = i + 1;
            if (arg instanceof String) {
                statement.setString(parameterIndex, (String) arg);
            } else if (arg instanceof Integer) {
                statement.setInt(parameterIndex, (Integer) arg);
            } else if (arg instanceof Long) {
                statement.setLong(parameterIndex, (Long) arg);
            } else {
                throw new IllegalArgumentException(String.format("Argument \"%s\" unsupported as SQL argument", arg));
            }
        }
    }
}

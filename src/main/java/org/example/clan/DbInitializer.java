package org.example.clan;

import org.example.clan.util.ConnectionManager;

import java.sql.SQLException;

public class DbInitializer {
    private final ConnectionManager connectionManager;

    public DbInitializer(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void createTables() throws SQLException {
        createUsersTable();
    }

    public void createUsersTable() throws SQLException {
        connectionManager.execute("CREATE TABLE users(id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(32) NOT NULL, gold INT NOT NULL)");
        connectionManager.execute("CREATE UNIQUE INDEX users_name_uidx ON users (name)");
    }
}

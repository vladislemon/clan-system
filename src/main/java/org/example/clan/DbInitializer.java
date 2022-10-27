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
        createClansTable();
        createTasksTable();
        createGoldTransactionsTable();
    }

    public void createUsersTable() throws SQLException {
        connectionManager.execute("CREATE TABLE users(id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(32) NOT NULL, gold INT NOT NULL)");
        connectionManager.execute("CREATE UNIQUE INDEX users_name_uidx ON users (name)");
    }

    public void createClansTable() throws SQLException {
        connectionManager.execute("CREATE TABLE clans(id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(32) NOT NULL, gold INT NOT NULL)");
        connectionManager.execute("CREATE UNIQUE INDEX clans_name_uidx ON clans (name)");
    }

    public void createTasksTable() throws SQLException {
        connectionManager.execute("CREATE TABLE tasks(id BIGINT AUTO_INCREMENT PRIMARY KEY, gold_reward INT NOT NULL)");
    }

    public void createGoldTransactionsTable() throws SQLException {
        connectionManager.execute("CREATE TABLE gold_transactions(id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "source_type VARCHAR(16) NOT NULL, source_id BIGINT NOT NULL, recipient_type VARCHAR(16) NOT NULL, " +
                "recipient_id BIGINT NOT NULL, amount INT NOT NULL, description VARCHAR(255), " +
                "created_at TIMESTAMP NOT NULL)");
        connectionManager.execute("CREATE INDEX gold_transactions_source_idx ON gold_transactions (source_id)");
        connectionManager.execute("CREATE INDEX gold_transactions_recipient_idx ON gold_transactions (recipient_id)");
    }
}

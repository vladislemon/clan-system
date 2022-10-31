package org.example.clan.clan;

import org.example.clan.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClanRepositoryImpl implements ClanRepository {
    private final ConnectionManager connectionManager;

    public ClanRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Clan getClan(long clanId) {
        String sql = "SELECT * FROM clans WHERE id = ?";
        try {
            return connectionManager.executeQuery(sql, this::extractClan, clanId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Clan> getAllClans() {
        String sql = "SELECT * FROM clans";
        try {
            return connectionManager.executeQuery(sql, this::extractClans);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Clan> findClanByName(String clanName) {
        String sql = "SELECT * FROM clans WHERE name = ?";
        try {
            return connectionManager.executeQuery(sql, this::extractClanOrEmpty, clanName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createClan(Clan clan) {
        String sql = "INSERT INTO clans (name, gold) VALUES (?, ?)";
        try {
            connectionManager.executeUpdate(sql, clan.getName(), clan.getGold());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateClan(Clan clan) {
        String sql = "UPDATE clans SET name = ?, gold = ? WHERE id = ?";
        try {
            connectionManager.executeUpdate(sql, clan.getName(), clan.getGold(), clan.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setClanGold(long clanId, int gold) {
        String sql = "UPDATE clans SET gold = ? WHERE id = ?";
        try {
            connectionManager.executeUpdate(sql, gold, clanId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Clan mapToClan(ResultSet resultSet) throws SQLException {
        return new Clan(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getInt("gold")
        );
    }

    private Optional<Clan> extractClanOrEmpty(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return Optional.of(mapToClan(resultSet));
        }
        return Optional.empty();
    }

    private Clan extractClan(ResultSet resultSet) throws SQLException {
        return extractClanOrEmpty(resultSet).orElseThrow(() -> new IllegalArgumentException("Clan not found"));
    }

    private List<Clan> extractClans(ResultSet resultSet) throws SQLException {
        List<Clan> clans = new ArrayList<>();
        while (resultSet.next()) {
            clans.add(mapToClan(resultSet));
        }
        return clans;
    }
}

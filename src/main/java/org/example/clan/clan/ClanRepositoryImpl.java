package org.example.clan.clan;

import org.example.clan.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public void createClan(Clan clan) {
        String sql = "INSERT INTO clans (name, gold) VALUES (?, ?)";
        try {
            connectionManager.executeUpdate(sql, clan.getName(), clan.getGold());
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

    private Clan extractClan(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return mapToClan(resultSet);
        }
        throw new IllegalArgumentException("Clan not found");
    }

    private List<Clan> extractClans(ResultSet resultSet) throws SQLException {
        List<Clan> clans = new ArrayList<>();
        while (resultSet.next()) {
            clans.add(mapToClan(resultSet));
        }
        return clans;
    }
}

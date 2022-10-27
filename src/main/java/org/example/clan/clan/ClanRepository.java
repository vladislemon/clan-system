package org.example.clan.clan;

import java.util.List;
import java.util.Optional;

public interface ClanRepository {

    Clan getClan(long clanId);

    List<Clan> getAllClans();

    Optional<Clan> findClanByName(String clanName);

    void createClan(Clan clan);

    void updateClan(Clan clan);
}

package org.example.clan.clan;

import java.util.List;
import java.util.Optional;

public interface ClanService {

    Clan getClan(long clanId);

    List<Clan> getAllClans();

    Optional<Clan> findClanByName(String clanName);

    void createClan(String clanName, int gold);

    void setClanGold(long clanId, int gold);

}

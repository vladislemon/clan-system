package org.example.clan.clan;

import java.util.List;

public interface ClanRepository {

    Clan getClan(long clanId);

    List<Clan> getAllClans();

    void createClan(Clan clan);

}

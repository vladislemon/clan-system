package org.example.clan.clan;

import java.util.List;

public interface ClanService {

    Clan getClan(long clanId);

    List<Clan> getAllClans();

    void createClan(String clanName);

}

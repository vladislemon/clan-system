package org.example.clan.clan;

import java.util.List;

public class ClanServiceImpl implements ClanService {
    private final ClanRepository clanRepository;

    public ClanServiceImpl(ClanRepository clanRepository) {
        this.clanRepository = clanRepository;
    }

    @Override
    public Clan getClan(long clanId) {
        return clanRepository.getClan(clanId);
    }

    @Override
    public List<Clan> getAllClans() {
        return clanRepository.getAllClans();
    }

    @Override
    public void createClan(String clanName) {
        Clan clan = new Clan(0, clanName, 0);
        clanRepository.createClan(clan);
    }
}

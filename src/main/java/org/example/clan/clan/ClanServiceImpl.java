package org.example.clan.clan;

import java.util.List;
import java.util.Optional;

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
    public Optional<Clan> findClanByName(String clanName) {
        return clanRepository.findClanByName(clanName);
    }

    @Override
    public void createClan(String clanName, int gold) {
        Clan clan = new Clan(0, clanName, gold);
        clanRepository.createClan(clan);
    }

    @Override
    public void setClanGold(long clanId, int gold) {
        Clan clan = getClan(clanId);
        clan.setGold(gold);
        clanRepository.updateClan(clan);
    }
}

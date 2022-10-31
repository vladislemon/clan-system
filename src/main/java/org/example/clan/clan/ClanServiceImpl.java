package org.example.clan.clan;

import org.example.clan.transaction.gold.GoldTransactionService;

import java.util.List;
import java.util.Optional;

public class ClanServiceImpl implements ClanService {
    private final ClanRepository clanRepository;
    private final GoldTransactionService goldTransactionService;

    public ClanServiceImpl(ClanRepository clanRepository, GoldTransactionService goldTransactionService) {
        this.clanRepository = clanRepository;
        this.goldTransactionService = goldTransactionService;
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
    public void createClan(String clanName, int gold) throws InterruptedException {
        Clan clan = new Clan(0, clanName, 0);
        clanRepository.createClan(clan);
        clan = findClanByName(clanName).orElseThrow(() -> new IllegalStateException("Clan nof found after creation"));
        goldTransactionService.addGoldToClan(clan.getId(), gold, "Initial gold");
    }
}

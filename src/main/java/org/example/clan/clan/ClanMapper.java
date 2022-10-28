package org.example.clan.clan;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ClanMapper {
    public ClanDto toDto(Clan clan) {
        return new ClanDto(clan.getName(), clan.getGold());
    }

    public List<ClanDto> toDtos(Collection<Clan> clans) {
        return clans.stream().map(this::toDto).collect(Collectors.toList());
    }
}

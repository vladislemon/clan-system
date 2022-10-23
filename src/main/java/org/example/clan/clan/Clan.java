package org.example.clan.clan;

import java.util.Objects;

public class Clan {
    private final long id;
    private String name;
    private int gold;

    public Clan(long id, String name, int gold) {
        this.id = id;
        this.name = name;
        this.gold = gold;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getGold() {
        return gold;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clan clan = (Clan) o;
        return id == clan.id && gold == clan.gold && name.equals(clan.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

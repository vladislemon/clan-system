package org.example.clan.task;

import java.util.Objects;

public class Task {
    private final long id;
    private final int goldReward;

    public Task(long id, int goldReward) {
        this.id = id;
        this.goldReward = goldReward;
    }

    public long getId() {
        return id;
    }

    public int getGoldReward() {
        return goldReward;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && goldReward == task.goldReward;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

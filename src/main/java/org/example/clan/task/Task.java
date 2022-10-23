package org.example.clan.task;

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
}

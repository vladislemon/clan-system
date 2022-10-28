package org.example.clan.task;

public class TaskDto {
    private int goldReward;

    public TaskDto(int goldReward) {
        this.goldReward = goldReward;
    }

    public int getGoldReward() {
        return goldReward;
    }

    public void setGoldReward(int goldReward) {
        this.goldReward = goldReward;
    }
}

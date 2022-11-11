package com.nyen.User.esalesawab.Model;

public class DailyRewardsModel {
    private String id;
    private String name;
    private String type;
    private long coins;
    private int finishedUnits;
    private int totalUnits;
    private String status;
    private Long time;

    public DailyRewardsModel() {
    }

    public DailyRewardsModel(String id, String name, String type, long coins, int finishedUnits, int totalUnits, String status, Long time) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.coins = coins;
        this.finishedUnits = finishedUnits;
        this.totalUnits = totalUnits;
        this.status = status;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public int getFinishedUnits() {
        return finishedUnits;
    }

    public void setFinishedUnits(int finishedUnits) {
        this.finishedUnits = finishedUnits;
    }

    public int getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(int totalUnits) {
        this.totalUnits = totalUnits;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}

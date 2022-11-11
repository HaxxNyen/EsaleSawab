package com.nyen.User.esalesawab.Model;

public class FundTransfer {

    private String trxId;
    private String description;
    private String byUserId;
    private String toUserId;
    private String coins;
    private String trxType;
    private String status;
    private long time;

    public FundTransfer() {
    }

    public FundTransfer(String trxId, String description, String byUserId, String toUserId, String coins, String trxType, String status, long time) {
        this.trxId = trxId;
        this.description = description;
        this.byUserId = byUserId;
        this.toUserId = toUserId;
        this.coins = coins;
        this.trxType = trxType;
        this.status = status;
        this.time = time;
    }

    public String getTrxId() {
        return trxId;
    }

    public String getDescription() {
        return description;
    }

    public String getByUserId() {
        return byUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public String getCoins() {
        return coins;
    }

    public String getTrxType() {
        return trxType;
    }

    public String getStatus() {
        return status;
    }

    public long getTime() {
        return time;
    }
}
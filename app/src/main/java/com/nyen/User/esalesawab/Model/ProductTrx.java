package com.nyen.User.esalesawab.Model;

import java.io.Serializable;

public class ProductTrx implements Serializable {
    private String id;
    private String name;
    private int coin;
    private int price;
    private String byUserID;
    private String walletName;
    private String trxID;
    private String status;
    private long time;

    public ProductTrx() {
    }

    public ProductTrx(String id, String name, int coin, int price, String byUserID, String walletName, String trxID, String status, long time) {
        this.id = id;
        this.name = name;
        this.coin = coin;
        this.price = price;
        this.byUserID = byUserID;
        this.walletName = walletName;
        this.trxID = trxID;
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

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getByUserID() {
        return byUserID;
    }

    public void setByUserID(String byUserID) {
        this.byUserID = byUserID;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getTrxID() {
        return trxID;
    }

    public void setTrxID(String trxID) {
        this.trxID = trxID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

package com.nyen.User.esalesawab.Model;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String name;
    private int coin;
    private int price;
    private String status;
    private long time;

    public Product() {
    }

    public Product(String id, String name, int coin, int price, String status, long time) {
        this.id = id;
        this.name = name;
        this.coin = coin;
        this.price = price;
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

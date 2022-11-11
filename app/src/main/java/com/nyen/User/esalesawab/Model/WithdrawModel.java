package com.nyen.User.esalesawab.Model;

import java.io.Serializable;

public class WithdrawModel implements Serializable {
    private String id;
    private String jcName;
    private String jcPhone;
    private String byUser;
    private String status;
    private String statusByUser;
    private int coins;
    private String percentage;
    private long time;

    public WithdrawModel() {
    }

    public WithdrawModel(String id, String jcName, String jcPhone, String byUser, String status, String statusByUser, int coins, String percentage, long time) {
        this.id = id;
        this.jcName = jcName;
        this.jcPhone = jcPhone;
        this.byUser = byUser;
        this.status = status;
        this.statusByUser = statusByUser;
        this.coins = coins;
        this.percentage = percentage;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJcName() {
        return jcName;
    }

    public void setJcName(String jcName) {
        this.jcName = jcName;
    }

    public String getJcPhone() {
        return jcPhone;
    }

    public void setJcPhone(String jcPhone) {
        this.jcPhone = jcPhone;
    }

    public String getByUser() {
        return byUser;
    }

    public void setByUser(String byUser) {
        this.byUser = byUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusByUser() {
        return statusByUser;
    }

    public void setStatusByUser(String statusByUser) {
        this.statusByUser = statusByUser;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

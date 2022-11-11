package com.nyen.User.esalesawab.Model;


// [START blog_user_class]

public class PremiumModel {


    private String id;
    private String byUser;
    private String status;
    private long expiry;
    private long time;

    public PremiumModel() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public PremiumModel(String id, String byUser, String status, long expiry, long time) {
        this.id = id;
        this.byUser = byUser;
        this.status = status;
        this.expiry = expiry;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public long getExpiry() {
        return expiry;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
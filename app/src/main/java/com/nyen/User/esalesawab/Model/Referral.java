package com.nyen.User.esalesawab.Model;


// [START blog_user_class]

public class Referral {


    public String id;
    public String phone;
    public String status;
    public long time;


    public Referral() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Referral(String id, String phone, String status, long time) {
        this.id = id;
        this.phone = phone;
        this.status = status;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
// [END blog_user_class]

package com.nyen.User.esalesawab.Model;

public class Marhoom {
    private String mID;
    private String mName;
    private String createdBy;
    private String nameAndUserId;
    private Long time;


    public Marhoom() {
    }

    public Marhoom(String mID, String mName, String createdBy, String nameAndUserId, Long time) {
        this.mID = mID;
        this.mName = mName;
        this.createdBy = createdBy;
        this.nameAndUserId = nameAndUserId;
        this.time = time;
    }

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getNameAndUserId() {
        return nameAndUserId;
    }

    public void setNameAndUserId(String nameAndUserId) {
        this.nameAndUserId = nameAndUserId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}

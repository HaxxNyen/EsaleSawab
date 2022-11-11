package com.nyen.User.esalesawab.Model;

public class BonusRedemption {

    private String id;
    private String userid;
    private String code;
    private String refererCode;
    private boolean status;
    private long time;

    public BonusRedemption() {
    }

    public BonusRedemption(String id, String userid, String code, String refererCode, boolean status, long time) {
        this.id = id;
        this.userid = userid;
        this.code = code;
        this.refererCode = refererCode;
        this.status = status;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRefererCode() {
        return refererCode;
    }

    public void setRefererCode(String refererCode) {
        this.refererCode = refererCode;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

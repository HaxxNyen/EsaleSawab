package com.nyen.User.esalesawab.Model;

import java.io.Serializable;

public class AcceptedESR implements Serializable {
    private String aID;
    private String esrid;
    private String status;
    private String byUser;
    private String byUserName;
    private String statusAndByUser;
    private long time;

    public AcceptedESR() {
    }

    public AcceptedESR(String aID, String esrid, String status, String byUser, String byUserName, String statusAndByUser, long time) {
        this.aID = aID;
        this.esrid = esrid;
        this.status = status;
        this.byUser = byUser;
        this.byUserName = byUserName;
        this.statusAndByUser = statusAndByUser;
        this.time = time;
    }

    public String getaID() {
        return aID;
    }

    public void setaID(String aID) {
        this.aID = aID;
    }

    public String getEsrid() {
        return esrid;
    }

    public void setEsrid(String esrid) {
        this.esrid = esrid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getByUser() {
        return byUser;
    }

    public void setByUser(String byUser) {
        this.byUser = byUser;
    }

    public String getByUserName() {
        return byUserName;
    }

    public void setByUserName(String byUserName) {
        this.byUserName = byUserName;
    }

    public String getStatusAndByUser() {
        return statusAndByUser;
    }

    public void setStatusAndByUser(String statusAndByUser) {
        this.statusAndByUser = statusAndByUser;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}


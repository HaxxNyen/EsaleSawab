package com.nyen.User.esalesawab.Model;

import java.io.Serializable;

public class EsaleSawabRequest implements Serializable {

    private String id;
    private String userID;
    private String esrname;
    private String articleID;
    private String reward;
    private String acceptedUnit;
    private String totalUnit;
    private String finishedUnit;
    private boolean isMarhoom;
    private String marhoomName;
    private String status;
    private Long esrtime;

    public EsaleSawabRequest() {
    }

    public EsaleSawabRequest(String id, String userID, String esrname, String articleID, String reward, String acceptedUnit, String totalUnit, String finishedUnit, boolean isMarhoom, String marhoomName, String status, Long esrtime) {
        this.id = id;
        this.userID = userID;
        this.esrname = esrname;
        this.articleID = articleID;
        this.reward = reward;
        this.acceptedUnit = acceptedUnit;
        this.totalUnit = totalUnit;
        this.finishedUnit = finishedUnit;
        this.isMarhoom = isMarhoom;
        this.marhoomName = marhoomName;
        this.status = status;
        this.esrtime = esrtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEsrname() {
        return esrname;
    }

    public void setEsrname(String esrname) {
        this.esrname = esrname;
    }

    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getAcceptedUnit() {
        return acceptedUnit;
    }

    public void setAcceptedUnit(String acceptedUnit) {
        this.acceptedUnit = acceptedUnit;
    }

    public String getTotalUnit() {
        return totalUnit;
    }

    public void setTotalUnit(String totalUnit) {
        this.totalUnit = totalUnit;
    }

    public String getFinishedUnit() {
        return finishedUnit;
    }

    public void setFinishedUnit(String finishedUnit) {
        this.finishedUnit = finishedUnit;
    }

    public boolean isMarhoom() {
        return isMarhoom;
    }

    public void setMarhoom(boolean marhoom) {
        isMarhoom = marhoom;
    }

    public String getMarhoomName() {
        return marhoomName;
    }

    public void setMarhoomName(String marhoomName) {
        this.marhoomName = marhoomName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getEsrtime() {
        return esrtime;
    }

    public void setEsrtime(Long esrtime) {
        this.esrtime = esrtime;
    }
}

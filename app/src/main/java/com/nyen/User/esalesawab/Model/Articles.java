package com.nyen.User.esalesawab.Model;

import java.io.Serializable;

public class Articles implements Serializable {
    public String aID;
    public String aName;
    public String aHadya;
    public String aCreatedBy;
    public Long aTime;

    public Articles() {
    }

    public Articles(String aID, String aName, String aHadya, String aCreatedBy, Long aTime) {
        this.aID = aID;
        this.aName = aName;
        this.aHadya = aHadya;
        this.aCreatedBy = aCreatedBy;
        this.aTime = aTime;
    }

    public String getaID() {
        return aID;
    }

    public String getaName() {
        return aName;
    }

    public String getaHadya() {
        return aHadya;
    }

    public String getaCreatedBy() {
        return aCreatedBy;
    }

    public Long getaTime() {
        return aTime;
    }
}

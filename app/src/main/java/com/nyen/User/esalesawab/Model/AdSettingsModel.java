package com.nyen.User.esalesawab.Model;

public class AdSettingsModel {
    private String id;
    private String name;
    private boolean adEnabled;
    private long time;

    public AdSettingsModel() {
    }

    public AdSettingsModel(String id, String name, boolean adEnabled, long time) {
        this.id = id;
        this.name = name;
        this.adEnabled = adEnabled;
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

    public boolean isAdEnabled() {
        return adEnabled;
    }

    public void setAdEnabled(boolean adEnabled) {
        this.adEnabled = adEnabled;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

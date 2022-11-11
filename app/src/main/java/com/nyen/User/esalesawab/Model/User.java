package com.nyen.User.esalesawab.Model;


// [START blog_user_class]

public class User {


    private String uid;
    private String name;
    private String phone;
    private String jcName;
    private String jcPhone;
    private int coins;
    private int xp;
    private long time;



    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uid, String name, String phone, String jcName, String jcPhone, int coins, int xp, long time) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.jcName = jcName;
        this.jcPhone = jcPhone;
        this.coins = coins;
        this.xp = xp;
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getJcName() {
        return jcName;
    }

    public String getJcPhone() {
        return jcPhone;
    }

    public int getCoins() {
        return coins;
    }

    public int getXp() {
        return xp;
    }

    public long getTime() {
        return time;
    }
}


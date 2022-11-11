package com.nyen.User.esalesawab.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private final Context context;

    public SharedPref(Context context){
        this.context = context;
    }

    public final static String PREFS_NAME = "EsaleSawab_prefs";

    public void setInt( String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(key, 0);
    }

    public void setLong( String key, Long value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public Long getLong(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getLong(key, 0);
    }

    public void setStr(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }


    public String getStr(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key,"DNF");
    }

    public void setBool(String key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBool(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getBoolean(key,false);
    }
}
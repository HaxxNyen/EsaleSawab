package com.nyen.User.esalesawab.Utils;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nyen.User.esalesawab.Database.FireBaseHelper;

public class AppUpdate {
    private final Context context;
    private final SharedPref sharedPref;
    private FireBaseHelper mDataBaseRef;
    private Integer FirebaseVersion = 1;
    private int currentVersion;

    public AppUpdate(Context context) {
        this.context = context;
        sharedPref = new SharedPref(context);
        mDataBaseRef = new FireBaseHelper(context);
        setFirebaseVersion();
    }

    public boolean getUpdateAvailable() {
        mCheckUpdate();
        return sharedPref.getBool(Cons.CHECK_UPDATE);

    }

    public void setUpdateAvailable(boolean b) {
        sharedPref.setBool(Cons.CHECK_UPDATE, b);
    }

    public void mCheckUpdate() {
        setUpdateAvailable(sharedPref.getInt(Cons.FIREBASE_APP_VERSION) > getCurrentVersion());
    }

    public int getCurrentVersion() {

        try {
            currentVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVersion;
    }

    public void setFirebaseVersion() {

        mDataBaseRef.getAppSettingsRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    FirebaseVersion = snapshot.child("versionCode").getValue(Integer.class);
                    sharedPref.setInt(Cons.FIREBASE_APP_VERSION, FirebaseVersion);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mDataBaseRef.ShowLog("Error: " + error);
            }
        });
    }
}

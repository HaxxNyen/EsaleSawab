package com.nyen.User.esalesawab.FCM;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nyen.User.esalesawab.Database.FireBaseHelper;

public class SubscribeToTopic {

    private final FireBaseHelper mDataBaseRef;


    public SubscribeToTopic(FireBaseHelper mDataBaseRef) {
        this.mDataBaseRef = mDataBaseRef;
    }

    public void setSubscriptionListener() {
        FirebaseMessaging.getInstance().subscribeToTopic("AllEsaleSawabUser")
                .addOnCompleteListener(task -> {
                    String msg = "Service subscribe";
                    if (!task.isSuccessful()) {
                        msg = "Service error";
                    }
                    mDataBaseRef.ShowLog(msg);
                });
    }

    public  void getToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            mDataBaseRef.ShowLog("Fetching FCM registration token failed"+ task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        // Log and toast
                        mDataBaseRef.ShowLog("Fetching FCM registration token: "+ token);
                    }
                });
    }


}

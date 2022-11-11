package com.nyen.User.esalesawab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.FCM.SubscribeToTopic;
import com.nyen.User.esalesawab.Utils.AppUpdate;
import com.nyen.User.esalesawab.UI.Registration.SignIn;
import com.nyen.User.esalesawab.UI.Registration.UpdateAppNow;
import com.suddenh4x.ratingdialog.AppRating.Builder;

import com.suddenh4x.ratingdialog.preferences.RatingThreshold;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private final Context context =MainActivity.this;
    private RewardedAd mRewardedAd;
    private FireBaseHelper mDataBaseRef;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Builder(this)
                .setMinimumLaunchTimes(1)
                .setMinimumDays(1)
                .setMinimumLaunchTimesToShowAgain(1)
                .setMinimumDaysToShowAgain(1)
                .setRatingThreshold(RatingThreshold.FOUR)
                .useGoogleInAppReview()
                .showIfMeetsConditions();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        mDataBaseRef = new FireBaseHelper(context);
        if (new AppUpdate(context).getUpdateAvailable()){

            startActivity(new Intent(context, UpdateAppNow.class));
            finish();
            mDataBaseRef.hideLoading();

        }
        // Call FCM Subscription Instance.
        SubscribeToTopic subscribeToTopic = new SubscribeToTopic(mDataBaseRef);
        subscribeToTopic.setSubscriptionListener();
        subscribeToTopic.getToken();



        MobileAds.initialize(this, initializationStatus -> {
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                 R.id.navigation_dashboard, R.id.navigation_home, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user==null){
            startActivity(new Intent(context, SignIn.class));
            finish();

        }
    }

}
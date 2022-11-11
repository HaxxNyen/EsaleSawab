package com.nyen.User.esalesawab.UI.Rewards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nyen.User.esalesawab.Adapters.CustomAdapterDailyReward;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.DailyRewardsModel;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.Utils.Cons;
import com.nyen.User.esalesawab.Utils.SharedPref;

import java.util.ArrayList;
import java.util.Locale;

public class DailyRewards extends AppCompatActivity  {

    private FireBaseHelper mDataBaseRef;
    private SharedPref sharedPref;
    Context context = DailyRewards.this;
    private static final long START_TIME_IN_MILLIS=1000*60*60*24;
    private long mTimeLeftInMillis = 0;
    private TextView tvCountDown;
    private RewardedAd mRewardedAd;
    private ConstraintLayout cvEmpty;
    private CustomAdapterDailyReward customAdapter;
    private final ArrayList<DailyRewardsModel> dailyRewardsModelModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_rewards);

        mDataBaseRef = new FireBaseHelper(context);
        sharedPref = new SharedPref(context);




        loadAd();

        if (sharedPref.getBool(Cons.SHARED_PREF_EXIST)){
            if (sharedPref.getBool(Cons.DAILY_REWARD+"Claim")){
                mTimeLeftInMillis = sharedPref.getLong(Cons.DAILY_REWARD+"time") + START_TIME_IN_MILLIS - System.currentTimeMillis();
                mClaimBtnDisable();
                mStartTimer();
            }
        }



        /*String Key = mDataBaseRef.getDailyRewardRef().push().getKey();
        DailyRewardsModel dailyRewardsModel = new DailyRewardsModel(Key,"Invite Login","Invite",100,0,3,Cons.PENDING, System.currentTimeMillis());
        mDataBaseRef.getDailyRewardRef().child(Key).setValue(dailyRewardsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    mDataBaseRef.ShowToast("Data Posted");
                }
            }
        });*/


        ListView listView = findViewById(R.id.lv);
        Button btnRefresh = findViewById(R.id.btnRefresh);
        cvEmpty = findViewById(R.id.emptyLayout);
        btnRefresh.setOnClickListener(view -> getDailyRewardList());
        customAdapter = new CustomAdapterDailyReward(context, dailyRewardsModelModelArrayList);
        listView.setAdapter(customAdapter);

        getDailyRewardList();












    }

    private void getDailyRewardList() {
        cvEmpty.setVisibility(View.GONE);
        dailyRewardsModelModelArrayList.clear();
        mDataBaseRef.showLoading();
        mDataBaseRef.getDailyRewardRef().orderByChild(Cons.STATUS).equalTo(Cons.PENDING)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                DailyRewardsModel dailyRewardsModel = ds.getValue(DailyRewardsModel.class);
                                assert dailyRewardsModel != null;
                                dailyRewardsModelModelArrayList.add(dailyRewardsModel);
                            }

                            customAdapter.notifyDataSetChanged();

                        } else {
                            cvEmpty.setVisibility(View.VISIBLE);


                        }
                        mDataBaseRef.hideLoading();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Error: " + error, Toast.LENGTH_SHORT).show();
                        mDataBaseRef.ShowLog("Error On Getting Product List: " + error);
                        mDataBaseRef.hideLoading();

                    }
                });
    }



    @SuppressLint("NonConstantResourceId")
/*    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDailyRewardsDailyLogin:
                if (!sharedPref.getBool(Cons.DAILY_REWARD+"Claim")){
                    sharedPref.setBool(Cons.SHARED_PREF_EXIST,true);
                    sharedPref.setBool(Cons.DAILY_REWARD+"Claim",true);
                    sharedPref.setLong(Cons.DAILY_REWARD+"time", System.currentTimeMillis());
                    mTimeLeftInMillis = START_TIME_IN_MILLIS;

                    mStartTimer();
                    mClaimBtnDisable();
                    Toast.makeText(context, "Reward not claimed already", Toast.LENGTH_SHORT).show();
                }
                mAddClaimBonus();
                break;

        }
    }*/
    private void mAddClaimBonus() {
        if (!sharedPref.getStr(Cons.DAILY_REWARD + "id").equals("DNF")){
            mDataBaseRef.getFundTransferRef().child(sharedPref.getStr(Cons.DAILY_REWARD+"id")).child("status").setValue(Cons.DAILY_REWARD+"_"+sharedPref.getStr(Cons.USER_ID)+"_Claimed");
        }
        mDataBaseRef.xyz("Received Daily Bonus","Paid Daily Login Bonus to "+sharedPref.getStr(Cons.USER_NAME),Cons.DAILY_LOGIN_REWARD,true, null, "Daily bonus claimed successfully.");
    }
    private void mStartTimer() {
        CountDownTimer mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                mUpdateCountDownText();

            }

            @Override
            public void onFinish() {
                //btnDailyRewardsDailyLogin.setEnabled(true);
                sharedPref.setBool(Cons.DAILY_REWARD + "Claim", false);

            }
        }.start();
    }
    private void mUpdateCountDownText() {


        long s = (mTimeLeftInMillis / 1000) % 60;
        long m = (mTimeLeftInMillis / (1000 * 60)) % 60;
        long h = mTimeLeftInMillis / (1000 * 60 * 60);

        String timeLeftFormatted = String.format(Locale.getDefault(),"%d:%02d:%02d",h,m,s);
        tvCountDown.setText(timeLeftFormatted);
    }
    private void mClaimBtnDisable() {
        //btnDailyRewardsDailyLogin.setEnabled(false);
    }
    private void loadAd() {
         /*  AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-1001585373006833/7737460655",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad was loaded.");


                    }
                });*/
    }
    private void showAd(){
            /*  if (mRewardedAd != null) {
                    Activity activityContext = DailyRewards.this;
                    mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.
                            Log.d(TAG, "The user earned the reward.");
                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();
                            Toast.makeText(context, "You Have earned "+ rewardAmount+" and type is "+ rewardType, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                }*/
    }
}
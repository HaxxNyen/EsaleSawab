package com.nyen.User.esalesawab.UI.Registration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Listener.AddBonusListener;
import com.nyen.User.esalesawab.Model.BonusRedemption;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.Utils.Cons;
import com.nyen.User.esalesawab.Utils.SharedPref;


public class Invite extends AppCompatActivity implements View.OnClickListener {

    private Button btnRedemption;
    private EditText etRedemptionCode;

    private FireBaseHelper mDataBaseRef;
    private final Context context = Invite.this;
    private SharedPref sharedPref;
    private AdView mAdview;
    private LinearLayout layoutInviteCode, mainLayout;
    private Button btnShareWithFriends;
    private TextView tvInviteCode;
    private TextView tvError;
    private ImageView ivInviteCodeCopyBtn;


    @SuppressLint({"SetTextI18n", "MissingPermission"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        initiateObjects();
        initiateIDs();
        initiateBtnListener();
        showAd();


        if (isSharedPrefExist()) {

            loadSettings();

        }else
        {
            setSharedPrefSettings();
        }
    }

    private void loadSettings() {

        tvInviteCode.setText(sharedPref.getStr(Cons.USER_CODE));
        layoutInviteCode.setVisibility(View.VISIBLE);
        btnShareWithFriends.setVisibility(View.VISIBLE);

        if (sharedPref.getBool(Cons.IS_CLAIMED+sharedPref.getStr(Cons.USER_ID))){
            mDataBaseRef.ShowLog("Reward Claimed already ");
            btnRedemption.setVisibility(View.GONE);
            etRedemptionCode.setVisibility(View.GONE);
        }
        mainLayout.setVisibility(View.VISIBLE);


    }

    private void setSharedPrefSettings() {

        mainLayout.setVisibility(View.GONE);
        mDataBaseRef.showLoading();
        mDataBaseRef.getBonusRedemptionRef().orderByChild(Cons.USER_ID).equalTo(sharedPref.getStr(Cons.USER_ID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    for (DataSnapshot ds : snapshot.getChildren()) {

                        BonusRedemption bonusRedemption = ds.getValue(BonusRedemption.class);
                        assert bonusRedemption != null;
                        sharedPref.setBool(Cons.IS_CLAIMED+sharedPref.getStr(Cons.USER_ID), bonusRedemption.isStatus());
                        sharedPref.setStr(Cons.USER_CODE_KEY, bonusRedemption.getId());
                        sharedPref.setStr(Cons.USER_CODE, bonusRedemption.getCode());
                        sharedPref.setBool(Cons.SHARED_PREF_EXIST + "Bonus"+sharedPref.getStr(Cons.USER_ID), true);
                        loadSettings();
                        mDataBaseRef.hideLoading();

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(context, "Error: " + error, Toast.LENGTH_SHORT).show();
                mDataBaseRef.ShowLog("Error On Getting setSharedPreSettings : " + error);
                sharedPref.setBool(Cons.SHARED_PREF_EXIST + "Bonus"+sharedPref.getStr(Cons.USER_ID), false);
                mDataBaseRef.hideLoading();

            }
        });
        mDataBaseRef.ShowLog("setSharedPrefSettings-End");
    }

    //https://dl.google.com/android/repository/sys-img/google_apis_playstore/x86_64-32_r02-windows.zip


    private void redeemBonus() {
        if (!sharedPref.getBool(Cons.IS_CLAIMED+sharedPref.getStr(Cons.USER_ID))) {
            mDataBaseRef.showLoading();
            mDataBaseRef.getBonusRedemptionRef().orderByChild(Cons.CODE).equalTo(getCode()).addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            BonusRedemption bonusRedemption = ds.getValue(BonusRedemption.class);
                            assert bonusRedemption != null;
                            if (bonusRedemption.getUserid().equals(sharedPref.getStr(Cons.USER_ID))) {
                                showError("Sorry! you are cheating.");
                            } else {

                                AddBonusListener addBonusListener = () -> {
                                    mDataBaseRef.xyz("Bonus on entering code","Bonus on entering code to ", Cons.REFERALL_BONUS, true, null, "Bonus added to your account.");
                                    sharedPref.setBool(Cons.IS_CLAIMED+sharedPref.getStr(Cons.USER_ID), true);
                                    btnRedemption.setVisibility(View.GONE);
                                    etRedemptionCode.setVisibility(View.GONE);
                                };
                                //first Bonus will be rewarded to referrer then user.
                                mDataBaseRef.mAddReferralBonus("Bonus on inviting to " + sharedPref.getStr(Cons.USER_NAME),
                                        "Bonus on inviting to " + sharedPref.getStr(Cons.USER_NAME) + " By " + getCode(),
                                        bonusRedemption.getUserid(), Cons.REFERALL_BONUS,addBonusListener);

                            }
                        }
                    } else {
                        mDataBaseRef.ShowLog("Invalid Code");
                        showError("Invalid Code");
                    }

                    mDataBaseRef.hideLoading();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Error: " + error, Toast.LENGTH_SHORT).show();
                    mDataBaseRef.ShowLog("Error On Getting setSharedPreSettings : " + error);

                    mDataBaseRef.hideLoading();

                }
            });


        }
        mDataBaseRef.ShowLog("redeemBonus-End");
    }
    private void showError(String msg) {
        tvError.setText(msg);
        tvError.setVisibility(View.VISIBLE);
        hideErrorTimer();
    }
    private void hideErrorTimer() {
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                tvError.setVisibility(View.GONE);

            }
        }.start();
    }
    private String getCode() {
        return etRedemptionCode.getText().toString();
    }
    private boolean isSharedPrefExist() {

        if (!sharedPref.getBool(Cons.SHARED_PREF_EXIST + "Bonus"+sharedPref.getStr(Cons.USER_ID))) {

            return false;
        }
        if (sharedPref.getStr(Cons.USER_CODE).equals("DNF")){
            return false;
        }
        return !sharedPref.getStr(Cons.USER_CODE_KEY).equals("DNF");

    }
    private void shareCodeWithFriends() {

        mDataBaseRef.shareAPP(sharedPref.getStr(Cons.USER_CODE));


    }


    @SuppressLint("MissingPermission")
    private void showAd() {

        if (sharedPref.getBool("adSettingsStatusInviteTopBannerAd")) {

            AdRequest adRequest = new AdRequest.Builder().build();
            mAdview.loadAd(adRequest);
            mAdview.setAdListener(new AdListener() {

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    AdRequest adRequest1 = new AdRequest.Builder().build();
                    mAdview.loadAd(adRequest1);

                }
            });

        } else {
            mAdview.setVisibility(View.GONE);
        }
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRedemption:
                if (getCode().length() != 9) {
                    etRedemptionCode.requestFocus();
                    etRedemptionCode.setError("Input a valid 9 digit invite code");
                    break;
                }
                redeemBonus();
                break;

            case R.id.btnSharewithFriends:
                shareCodeWithFriends();
                break;

            case R.id.ivInviteCodeCopyBtn:
                copyCodeToClipBoard(sharedPref.getStr(Cons.USER_CODE));
                break;
        }

    }

    private void copyCodeToClipBoard(String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
        mDataBaseRef.ShowToast("Copied: "+text);


    }
    private void initiateBtnListener() {
        btnRedemption.setOnClickListener(this);
        btnShareWithFriends.setOnClickListener(this);
        ivInviteCodeCopyBtn.setOnClickListener(this);
    }
    private void initiateIDs() {
        btnRedemption = (Button) findViewById(R.id.btnRedemption);
        btnShareWithFriends = (Button) findViewById(R.id.btnSharewithFriends);
        tvError = (TextView) findViewById(R.id.tvError);
        tvInviteCode = (TextView) findViewById(R.id.tvInviteCode);
        ivInviteCodeCopyBtn = (ImageView) findViewById(R.id.ivInviteCodeCopyBtn);
        etRedemptionCode = (EditText) findViewById(R.id.etRedemptionCode);
        layoutInviteCode = (LinearLayout) findViewById(R.id.layoutInviteCode);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        mAdview = findViewById(R.id.mAdview6);
    }
    private void initiateObjects() {
        mDataBaseRef = new FireBaseHelper(this);
        sharedPref = new SharedPref(context);

    }
}
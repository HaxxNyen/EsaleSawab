package com.nyen.User.esalesawab.UI.Fund;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
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
import com.nyen.User.esalesawab.Model.WithdrawModel;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.Utils.Cons;
import com.nyen.User.esalesawab.Utils.SharedPref;
import com.nyen.User.esalesawab.UI.Profile.Profile;


import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Withdraw extends AppCompatActivity implements View.OnClickListener {
    private FireBaseHelper mDataBaseRef;
    private final Context context = Withdraw.this;

    private int mCoinsWithdraw = 0;
    private SharedPref sharedPref;
    private Integer btnOneValue, btnTwoValue, btnThreeValue, btnFourValue, btnFiveValue, btnSixValue;
    private Button btnOne;
    private Button btnTwo;
    private Button btnThree;
    private Button btnFour;
    private Button btnFive;
    private Button btnSix;
    private double mPercentage;
    private String mPercentageTxt;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        mDataBaseRef = new FireBaseHelper(this);
        sharedPref = new SharedPref(context);

        Button btnPendingWithdraw = findViewById(R.id.btnPendingWithdraw);
        btnOne = findViewById(R.id.withdrawFirstBtn);
        btnTwo = findViewById(R.id.withdrawSecondBtn);
        btnThree = findViewById(R.id.withdrawThirdBtn);
        btnFour = findViewById(R.id.withdrawFourthBtn);
        btnFive = findViewById(R.id.withdrawFifthBtn);
        btnSix = findViewById(R.id.withdrawSixthBtn);
        AdView mAdview = findViewById(R.id.mAdview8);

        if (sharedPref.getBool("adSettingsStatusWithdrawBottomMediumRectangleAd")) {

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


        mDisableBtn();
        mDataBaseRef.showLoading();
        UpdateUi();


        btnPendingWithdraw.setOnClickListener(this);
        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
        btnFour.setOnClickListener(this);
        btnFive.setOnClickListener(this);
        btnSix.setOnClickListener(this);

    }

    private void mDisableBtn() {
        btnOne.setEnabled(false);
        btnTwo.setEnabled(false);
        btnThree.setEnabled(false);
        btnFour.setEnabled(false);
        btnFive.setEnabled(false);
        btnSix.setEnabled(false);
    }

    private void mEnabledBtn() {
        btnOne.setEnabled(true);
        btnTwo.setEnabled(true);
        btnThree.setEnabled(true);
        btnFour.setEnabled(true);
        btnFive.setEnabled(true);
        btnSix.setEnabled(true);
    }

    private void UpdateUi() {
        mDataBaseRef.getWithdrawValueRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    btnOneValue = snapshot.child("BtnOne").getValue(Integer.class);
                    btnTwoValue = snapshot.child("BtnTwo").getValue(Integer.class);
                    btnThreeValue = snapshot.child("BtnThree").getValue(Integer.class);
                    btnFourValue = snapshot.child("BtnFour").getValue(Integer.class);
                    btnFiveValue = snapshot.child("BtnFive").getValue(Integer.class);
                    btnSixValue = snapshot.child("BtnSix").getValue(Integer.class);
                    mSetValueToBtn();
                    mEnabledBtn();
                }
                mDataBaseRef.hideLoading();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error: " + error, Toast.LENGTH_SHORT).show();
                mDataBaseRef.hideLoading();

            }

        });
    }

    @SuppressLint("SetTextI18n")
    private void mSetValueToBtn() {

        btnOne.setText(btnOneValue + " Coins");
        btnTwo.setText(btnTwoValue + " Coins");
        btnThree.setText(btnThreeValue + " Coins");
        btnFour.setText(btnFourValue + " Coins");
        btnFive.setText(btnFiveValue + " Coins");
        btnSix.setText(btnSixValue + " Coins");

    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.withdrawFirstBtn:
                mCoinsWithdraw = btnOneValue;
                mPercentage = 0.6;
                mPercentageTxt = "40%";
                mAddWithdrawRequest();
                break;
            case R.id.withdrawSecondBtn:
                mCoinsWithdraw = btnTwoValue;
                mPercentage = 0.7;
                mPercentageTxt = "30%";
                mAddWithdrawRequest();
                break;
            case R.id.withdrawThirdBtn:
                mCoinsWithdraw = btnThreeValue;
                mPercentage = 0.8;
                mPercentageTxt = "20%";
                mAddWithdrawRequest();
                break;
            case R.id.withdrawFourthBtn:
                mCoinsWithdraw = btnFourValue;
                mPercentage = 0.9;
                mPercentageTxt = "10%";
                mAddWithdrawRequest();

                break;
            case R.id.withdrawFifthBtn:
                mCoinsWithdraw = btnFiveValue;
                mPercentage = 0.95;
                mPercentageTxt = "5%";
                mAddWithdrawRequest();

                break;
            case R.id.withdrawSixthBtn:
                mCoinsWithdraw = btnSixValue;
                mPercentage = 0.98;
                mPercentageTxt = "2%";
                mAddWithdrawRequest();
                break;

            case R.id.btnPendingWithdraw:
                Intent intent = new Intent(context, PendingWithdraw.class);
                startActivity(intent);


        }


    }

    private void mAddWithdrawRequest() {

        if (sharedPref.getStr(Cons.USER_JC_NAME).equals("DNF") || sharedPref.getStr(Cons.USER_JC_NAME).equals("")) {
            new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText("Error")
                    .setContentText("Jazz Cash Title is missing.")
                    .setCancelText("No,Thanks!")
                    .setConfirmText("Update")
                    .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                    .setConfirmClickListener(sweetAlertDialog -> {
                        sweetAlertDialog.dismissWithAnimation();
                        Intent intent = new Intent(context, Profile.class);
                        startActivity(intent);
                        finish();

                    })

                    .show();

            return;

        }
        if (sharedPref.getStr(Cons.USER_JC_PHONE).equals("DNF") || sharedPref.getStr(Cons.USER_JC_PHONE).equals("")) {
            new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText("Error")
                    .setContentText("Jazz Cash Number is missing.")
                    .setCancelText("No,Thanks!")
                    .setConfirmText("Update")
                    .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                    .setConfirmClickListener(sweetAlertDialog -> {
                        sweetAlertDialog.dismissWithAnimation();
                        Intent intent = new Intent(context, Profile.class);
                        startActivity(intent);
                        finish();
                    })

                    .show();

            return;

        }


        if (mCoinsWithdraw > sharedPref.getInt(Cons.USER_COINS)) {
            new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText(Cons.INSUFFICIENT_FUND)
                    .setContentText(Cons.INSUFFICIENT_FUND_DESCRIPTION)
                    .setCancelText("No,Thanks!")
                    .setConfirmText(Cons.BUY_NOW)
                    .setCustomImage(R.drawable.ic_dollar)

                    .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                    .setConfirmClickListener(sweetAlertDialog -> {
                        sweetAlertDialog.dismissWithAnimation();

                        Intent intent = new Intent(context, Store.class);
                        startActivity(intent);
                        finish();

                    })
                    .show();

            return;
        }


        double Rs = (mCoinsWithdraw * Cons.CONVERSATION_RATE) * mPercentage;
        new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Confirmation")
                .setContentText("Do you want to withdraw: " + mCoinsWithdraw + " coins for Rs: " + Rs + " with " + mPercentageTxt + " service charges?")
                .setCancelText("No,Thanks!")
                .setConfirmText("Withdraw")
                .setCustomImage(R.drawable.ic_dollar)

                .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                .setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    mDataBaseRef.showLoading();
                    String key = mDataBaseRef.getWithdrawRef().push().getKey();


                    //TODO Minus Percentage from mCOinWithdraw...
                    WithdrawModel withdrawModel = new WithdrawModel(key, sharedPref.getStr(Cons.USER_JC_NAME), sharedPref.getStr(Cons.USER_JC_PHONE), sharedPref.getStr(Cons.USER_ID), Cons.PENDING, Cons.PENDING + "_" + sharedPref.getStr(Cons.USER_ID), mCoinsWithdraw, mPercentageTxt, System.currentTimeMillis());
                    Map<String, Object> childUpdate = new HashMap<>();
                    childUpdate.put("/Withdraw/" + key, withdrawModel);
                    mDataBaseRef.xyz("Withdraw Request", "Withdraw Request", mCoinsWithdraw, false, childUpdate, "Withdraw request submitted. ");
                    mDataBaseRef.hideLoading();
                })
                .show();


    }

}
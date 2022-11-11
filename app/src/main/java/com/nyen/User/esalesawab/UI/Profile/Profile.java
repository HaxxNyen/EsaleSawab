package com.nyen.User.esalesawab.UI.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.MainActivity;
import com.nyen.User.esalesawab.Model.User;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.Utils.Cons;
import com.nyen.User.esalesawab.Utils.SharedPref;

public class Profile extends AppCompatActivity {
    Context context = Profile.this;
    private EditText etProfileUserName, etProfileUserPhone, etProfileUserJcName, etProfileUserJcPhone;
    private Button btnProfileUpdate;
    private SharedPref sharedPref;
    private FireBaseHelper mDataBaseRef;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        etProfileUserName = findViewById(R.id.etProfileUserName);
        etProfileUserPhone = findViewById(R.id.etProfileUserPhone);
        etProfileUserJcName = findViewById(R.id.etProfileUserJcName);
        etProfileUserJcPhone = findViewById(R.id.etProfileUserJcPhone);
        btnProfileUpdate = findViewById(R.id.btnProfileUpdate);
        mDataBaseRef = new FireBaseHelper(this);
        sharedPref = new SharedPref(context);

        AdView mAdview =  findViewById(R.id.mAdview9);

        if (sharedPref.getBool("adSettingsStatusProfileBottomMediumRectangleAd")){

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

        }else {
            mAdview.setVisibility(View.GONE);
        }


        mUpdateEditText();

        btnProfileUpdate.setOnClickListener(v -> {

            if (etProfileUserName.getText().toString().length() < 3) {
                etProfileUserName.requestFocus();
                etProfileUserName.setError("Please Input User Name");
                return;
            }

            if (etProfileUserJcName.getText().toString().length() < 3) {
                etProfileUserJcName.requestFocus();
                etProfileUserJcName.setError("Please Input Jazz Cash Account title. ");
                return;
            }
            if (etProfileUserJcPhone.getText().toString().length() != 11) {
                etProfileUserJcPhone.requestFocus();
                etProfileUserJcPhone.setError("Please Input Valid Jazz Cash Account Number.");
                return;
            }

            mDataBaseRef.showLoading();
            btnProfileUpdate.setEnabled(false);
            User user = new User(sharedPref.getStr(Cons.USER_ID), etProfileUserName.getText().toString(),
                    etProfileUserPhone.getText().toString(), etProfileUserJcName.getText().toString(),
                    etProfileUserJcPhone.getText().toString(), sharedPref.getInt(Cons.USER_COINS), sharedPref.getInt(Cons.USER_XP), sharedPref.getLong(Cons.USER_TIME));

            mDataBaseRef.getUsersRef().child(sharedPref.getStr(Cons.USER_ID)).setValue(user).addOnCompleteListener(task -> {

                if (task.isSuccessful()){
                    sharedPref.setStr(Cons.USER_ID, user.getUid());
                    sharedPref.setStr(Cons.USER_NAME, user.getName());
                    sharedPref.setStr(Cons.USER_PHONE, user.getPhone());
                    sharedPref.setStr(Cons.USER_JC_NAME, user.getJcName());
                    sharedPref.setStr(Cons.USER_JC_PHONE, user.getJcPhone());
                    sharedPref.setInt(Cons.USER_COINS, user.getCoins());
                    sharedPref.setInt(Cons.USER_XP, user.getXp());
                    sharedPref.setLong(Cons.USER_TIME, user.getTime());

                    Toast.makeText(context, "Updated Successfully.", Toast.LENGTH_SHORT).show();
                    mGotoMainActivity();


                }
                else {
                    Toast.makeText(context, "Something wrong in updating profile.", Toast.LENGTH_SHORT).show();
                }
                mDataBaseRef.hideLoading();
                btnProfileUpdate.setEnabled(true);
            });

        });

    }

    private void mUpdateEditText() {

        etProfileUserName.setText(sharedPref.getStr(Cons.USER_NAME));
        etProfileUserPhone.setText(sharedPref.getStr(Cons.USER_PHONE));
        if (!sharedPref.getStr(Cons.USER_JC_NAME).equals("DNF")){
            etProfileUserJcName.setText(sharedPref.getStr(Cons.USER_JC_NAME));
        }
        if (!sharedPref.getStr(Cons.USER_JC_PHONE).equals("DNF")){
            etProfileUserJcPhone.setText(sharedPref.getStr(Cons.USER_JC_PHONE));
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(sharedPref.getBool("profileCameFrom")){
            mGotoMainActivity();
        }

    }

    private void mGotoMainActivity() {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
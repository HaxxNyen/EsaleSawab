package com.nyen.User.esalesawab.UI.Registration;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nyen.User.esalesawab.Adapters.CustomAdapterReferral;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.Referral;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.Utils.SharedPref;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class ReferralList extends AppCompatActivity {

    private final Context context = ReferralList.this;
    private final ArrayList<Referral> referralModelArrayList = new ArrayList<>();
    private CustomAdapterReferral customAdapter;
    private FireBaseHelper mDataBaseRef;
    private ConstraintLayout cvEmpty;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_list);

        SharedPref sharedPref = new SharedPref(context);
        ListView listView = findViewById(R.id.lv_referral);
        cvEmpty = findViewById(R.id.emptyLayout);

        mDataBaseRef = new FireBaseHelper(context);
        AdView mAdview =  findViewById(R.id.mAdview7);

        if (sharedPref.getBool("adSettingsStatusReferralListTopBannerAd")){

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

        mDataBaseRef.showLoading();


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        mDataBaseRef.getReferralRef().orderByChild("id").equalTo(uId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Referral referral = ds.getValue(Referral.class);
                        referralModelArrayList.add(referral);
                    }
                    Collections.reverse(referralModelArrayList);
                    mDataBaseRef.hideLoading();
                    customAdapter.notifyDataSetChanged();
                } else {
                    mDataBaseRef.hideLoading();
                    cvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mDataBaseRef.hideLoading();

            }
        });
        customAdapter = new CustomAdapterReferral(context, referralModelArrayList);
        listView.setAdapter(customAdapter);


    }


}

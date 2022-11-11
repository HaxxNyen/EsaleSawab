package com.nyen.User.esalesawab.UI.Fund;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nyen.User.esalesawab.Adapters.CustomAdapterViewPointHistoryList;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.FundTransfer;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.Utils.SharedPref;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ViewPointsHistory extends AppCompatActivity {

    private FireBaseHelper mDataBaseRef;
    private FirebaseAuth mAuth;
    private final ArrayList<FundTransfer> ViewPointsHistoryModelArrayList = new ArrayList<>();
    private final Context context = ViewPointsHistory.this;
    private CustomAdapterViewPointHistoryList customAdapter;
    private SharedPref sharedPref;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_points_history);
        sharedPref = new SharedPref(context);


        ImageButton ibBtnRefresh =  findViewById(R.id.ibRefreshVeiwPintHistory);
        ListView listView =  findViewById(R.id.lv_ViewPointHistoryList);

        AdView mAdview =  findViewById(R.id.mAdview10);

        if (sharedPref.getBool("adSettingsStatusViewPointsHistoryTopBannerAd")){

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


        mDataBaseRef = new FireBaseHelper(this);
        mAuth = FirebaseAuth.getInstance();
        getPointHistory();

        ibBtnRefresh.setOnClickListener(v -> {
            ViewPointsHistoryModelArrayList.clear();
            getPointHistory();
        });
        customAdapter = new CustomAdapterViewPointHistoryList(context, ViewPointsHistoryModelArrayList);
        listView.setAdapter(customAdapter);
    }



    //Functions
    private void getPointHistory() {

        mDataBaseRef.showLoading();
        mDataBaseRef.getFundTransferRef().orderByChild("toUserId")
                .equalTo(Objects.requireNonNull(mAuth.getCurrentUser())
                        .getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    for (DataSnapshot ds:snapshot.getChildren()){
                        FundTransfer fundTransfer = ds.getValue(FundTransfer.class);

                        assert fundTransfer != null;

                        ViewPointsHistoryModelArrayList.add(fundTransfer);

                    }
                    if(ViewPointsHistoryModelArrayList.size()>0){
                        Collections.reverse(ViewPointsHistoryModelArrayList);

                        customAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    Toast.makeText(context, "No Transaction found.", Toast.LENGTH_SHORT).show();
                }
                mDataBaseRef.hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error: "+ error, Toast.LENGTH_SHORT).show();
                mDataBaseRef.hideLoading();

            }
        });

    }
}
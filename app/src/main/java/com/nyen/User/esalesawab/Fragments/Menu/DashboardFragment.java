package com.nyen.User.esalesawab.Fragments.Menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.AdSettingsModel;
import com.nyen.User.esalesawab.Model.BonusRedemption;
import com.nyen.User.esalesawab.Model.FundTransfer;
import com.nyen.User.esalesawab.Model.User;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.UI.Fund.ViewPointsHistory;
import com.nyen.User.esalesawab.UI.Rewards.DailyRewards;
import com.nyen.User.esalesawab.Utils.Cons;
import com.nyen.User.esalesawab.Utils.SharedPref;
import com.nyen.User.esalesawab.UI.Profile.Profile;
import com.nyen.User.esalesawab.UI.Registration.SignIn;

import java.util.Objects;
public class DashboardFragment extends Fragment {
    private FireBaseHelper mDataBaseRef;
    private String points;
    private TextView etDashboardPoints, tvDashboardUserName, tvDashboardUserPhone;
    private ScrollView layoutDashboard;
    private Context context;
    private SharedPref sharedPref;
    private AdView advDashboardTop;
    private String userId;


    @SuppressLint("MissingPermission")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        context = this.getContext();
        sharedPref = new SharedPref(context);
        mDataBaseRef = new FireBaseHelper(context);


        advDashboardTop =  root.findViewById(R.id.advDashboardTop);
        etDashboardPoints =  root.findViewById(R.id.dashboardPoints);
        TextView tvViewPointHistory =  root.findViewById(R.id.tvviewPointHistory);
        tvDashboardUserName =  root.findViewById(R.id.tvDashboardUserName);
        tvDashboardUserPhone =  root.findViewById(R.id.tvDashboardUserPhone);
        layoutDashboard =  root.findViewById(R.id.dashboardLayout);





        //Display Ad
        if (sharedPref.getBool("adSettingsStatusDashboardFragmentBottomBannerAd")){

            AdRequest adRequest = new AdRequest.Builder().build();
            /*advDashboardTop.setAdUnitId(Cons.DASHBOARD_TOP_BANNER_AD_UNIT_ID);*/
            advDashboardTop.loadAd(adRequest);
            advDashboardTop.setAdListener(new AdListener() {

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    /*AdRequest adRequest1 = new AdRequest.Builder().build();
                    advDashboardTop.loadAd(adRequest1);*/

                }
            });

        }else {
            advDashboardTop.setVisibility(View.GONE);
        }


        ImageView IvDashboardUserProfileEdit =  root.findViewById(R.id.IvDashboardUserProfileEdit);
        Button btnDashboardDailyReward = root.findViewById(R.id.btnDashboardDailyReward);
        btnDashboardDailyReward.setOnClickListener(v -> {
            Intent intent = new Intent(context, DailyRewards.class);
            startActivity(intent);
        });
        IvDashboardUserProfileEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, Profile.class);
            startActivity(intent);
            requireActivity().finish();
            sharedPref.setBool("profileCameFrom",true);
        });


        mDataBaseRef.showLoading();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null){
            startActivity(new Intent(getContext(), SignIn.class));
            requireActivity().finish();
            return root;
        }
        tvViewPointHistory.setOnClickListener(v -> startActivity(new Intent(getContext(), ViewPointsHistory.class)));


        userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mDataBaseRef.getUsersRef().orderByChild("uid").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                        User user = ds.getValue(User.class);
                        assert user != null;

                        //points variable for EditText Box
                        points = String.valueOf(user.getCoins());

                        sharedPref.setStr(Cons.USER_ID, user.getUid());
                        sharedPref.setStr(Cons.USER_NAME, user.getName());
                        sharedPref.setStr(Cons.USER_PHONE, user.getPhone());
                        sharedPref.setStr(Cons.USER_JC_NAME, user.getJcName());
                        sharedPref.setStr(Cons.USER_JC_PHONE, user.getJcPhone());
                        sharedPref.setInt(Cons.USER_COINS, user.getCoins());
                        sharedPref.setInt(Cons.USER_XP, user.getXp());
                        sharedPref.setLong(Cons.USER_TIME, user.getTime());
                        getAdminBalance();
                        getTimeInMillisAndId();
                        getAdSettings();
                        setInviteSettings();
                    }
                }
                etDashboardPoints.setText(points);
                tvDashboardUserName.setText(sharedPref.getStr(Cons.USER_NAME));
                tvDashboardUserPhone.setText(sharedPref.getStr(Cons.USER_PHONE));
                layoutDashboard.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mDataBaseRef.hideLoading();
                Toast.makeText(context, "Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    private void getAdminBalance() {

        mDataBaseRef.getUsersRef().orderByChild(Cons.USER_PHONE).equalTo("03027860317").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                        User userAdmin = ds.getValue(User.class);
                        assert userAdmin != null;
                        sharedPref.setStr(Cons.ADMIN_ID, userAdmin.getUid());
                        sharedPref.setStr(Cons.ADMIN_NAME, userAdmin.getName());
                        sharedPref.setStr(Cons.ADMIN_PHONE, userAdmin.getPhone());
                        sharedPref.setInt(Cons.ADMIN_COINS, userAdmin.getCoins());
                        sharedPref.setInt(Cons.ADMIN_XP, userAdmin.getXp());
                    }
                }
                else {
                    Toast.makeText(context, "Admin not Exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "On Error: "+ error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTimeInMillisAndId() {
        mDataBaseRef.getFundTransferRef().orderByChild("status").equalTo(Cons.DAILY_REWARD +"_"+sharedPref.getStr(Cons.USER_ID)+"_ClaimedLast").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                        FundTransfer fundTransfer = ds.getValue(FundTransfer.class);
                        sharedPref.setBool(Cons.SHARED_PREF_EXIST,true);
                        assert fundTransfer != null;
                        sharedPref.setStr(Cons.DAILY_REWARD+"id",fundTransfer.getTrxId());
                        sharedPref.setLong(Cons.DAILY_REWARD+"time",fundTransfer.getTime());
                        sharedPref.setBool(Cons.DAILY_REWARD+"Claim",true);
                    }
                }
                else {
                    sharedPref.setBool(Cons.DAILY_REWARD+"Claim",false);
                }
                mDataBaseRef.hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "On Error: "+ error, Toast.LENGTH_SHORT).show();
                mDataBaseRef.hideLoading();


            }
        });
    }

    private void getAdSettings() {
        mDataBaseRef.getAdSettingsRef().orderByChild("id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){

                        AdSettingsModel adSettingsModel = ds.getValue(AdSettingsModel.class);
                        assert adSettingsModel != null;

                        sharedPref.setBool("adSettingsStatus"+adSettingsModel.getName(),adSettingsModel.isAdEnabled());
                    }
                }


                mDataBaseRef.hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "On Error: "+ error, Toast.LENGTH_SHORT).show();
                mDataBaseRef.hideLoading();


            }
        });
    }






    private void setInviteSettings() {
        mDataBaseRef.getBonusRedemptionRef().orderByChild(Cons.USER_ID).equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        BonusRedemption bonusRedemption = ds.getValue(BonusRedemption.class);
                        assert bonusRedemption != null;
                        sharedPref.setBool(Cons.IS_CLAIMED+userId, bonusRedemption.isStatus());
                        sharedPref.setStr(Cons.USER_CODE_KEY, bonusRedemption.getId());
                        sharedPref.setStr(Cons.USER_CODE, bonusRedemption.getCode());
                    }
                }
                sharedPref.setBool(Cons.SHARED_PREF_EXIST + "Bonus"+userId, true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(context, "Error: " + error, Toast.LENGTH_SHORT).show();
                mDataBaseRef.ShowLog("Error On Getting setSharedPreSettings : " + error);
                sharedPref.setBool(Cons.SHARED_PREF_EXIST + "Bonus"+userId, false);

            }
        });
    }

}
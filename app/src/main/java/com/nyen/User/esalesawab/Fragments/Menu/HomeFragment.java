package com.nyen.User.esalesawab.Fragments.Menu;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.firebase.auth.FirebaseAuth;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.UI.Fund.PlayStore;
import com.nyen.User.esalesawab.UI.Fund.Store;
import com.nyen.User.esalesawab.UI.Fund.Withdraw;
import com.nyen.User.esalesawab.UI.Profile.AddMarhoom;
import com.nyen.User.esalesawab.UI.Registration.Invite;
import com.nyen.User.esalesawab.UI.Registration.ReferralList;
import com.nyen.User.esalesawab.UI.Task.AddArticles;
import com.nyen.User.esalesawab.UI.Task.MyTask;
import com.nyen.User.esalesawab.Utils.SharedPref;

import com.nyen.User.esalesawab.UI.Profile.Profile;
import com.nyen.User.esalesawab.UI.Registration.SignIn;


public class HomeFragment extends Fragment {



    private CardView btnLogout, btnInvite, btnStore,btnPlayStore, btnWithdraw, btnProfile, btnRefer, btnAddMarhoom, btnarticle, btnMyTask, btnPrivacyPolicy;
    private FirebaseAuth mAuth;
    private SharedPref sharedPref;
    private Context context;
    private AdView mAdview;
    ;


    @SuppressLint("MissingPermission")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        context = this.getContext();
        mAuth = FirebaseAuth.getInstance();
        sharedPref = new SharedPref(context);



        btnAddMarhoom = (CardView) root.findViewById(R.id.btnAddMarhoom);
        btnarticle = (CardView) root.findViewById(R.id.btnarticle);
        btnStore = (CardView) root.findViewById(R.id.btnStore);
        btnPlayStore = (CardView) root.findViewById(R.id.btnPlayStore);
        btnWithdraw = (CardView) root.findViewById(R.id.btnWithdraw);
        btnProfile = (CardView) root.findViewById(R.id.btnProfile);
        btnInvite = (CardView) root.findViewById(R.id.btnInvite);
        btnRefer = (CardView) root.findViewById(R.id.btnRefer);
        btnMyTask = (CardView) root.findViewById(R.id.btnMyTask);
        btnPrivacyPolicy = (CardView) root.findViewById(R.id.btnPrivacyPolicy);
        btnLogout = (CardView) root.findViewById(R.id.btnLogout);
        mAdview =  root.findViewById(R.id.mAdview);

        if (sharedPref.getBool("adSettingsStatusHomeFragmentTopBannerAd")){

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




        btnAddMarhoom.setOnClickListener(view -> startActivity(new Intent(getActivity(), AddMarhoom.class)));
        btnarticle.setOnClickListener(view -> startActivity(new Intent(getActivity(), AddArticles.class)));
        btnStore.setOnClickListener(view -> startActivity(new Intent(getActivity(), Store.class)));
        btnPlayStore.setOnClickListener(view -> startActivity(new Intent(getActivity(), PlayStore.class)));
        btnWithdraw.setOnClickListener(view -> startActivity(new Intent(getActivity(), Withdraw.class)));
        btnProfile.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), Profile.class));
            sharedPref.setBool("profileCameFrom",false);

        });
        btnInvite.setOnClickListener(view -> startActivity(new Intent(getActivity(), Invite.class)));
        btnRefer.setOnClickListener(view -> startActivity(new Intent(getActivity(), ReferralList.class)));
        btnMyTask.setOnClickListener(view -> startActivity(new Intent(getActivity(), MyTask.class)));
        btnPrivacyPolicy.setOnClickListener(view -> openLink());
        btnLogout.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(getActivity(), SignIn.class));

        });


        return root;
    }

    private void openLink() {
        String url = "https://docs.google.com/document/d/1NgS5gaC859pA_Z2k0ta0YatcvuetOQJXqrfvZGErK_c/edit";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
package com.nyen.User.esalesawab.Fragments.Menu;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.nyen.User.esalesawab.Adapters.CustomAdapterESRList;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.AcceptedESR;
import com.nyen.User.esalesawab.Model.EsaleSawabRequest;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.UI.Task.AcceptedTask;
import com.nyen.User.esalesawab.Utils.Cons;
import com.nyen.User.esalesawab.Utils.SharedPref;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {


    private Context context;
    private FireBaseHelper mDataBaseRef;
    private CustomAdapterESRList customAdapter;
    private final ArrayList<EsaleSawabRequest> ESRModelArrayList = new ArrayList<>();
    private SharedPref sharedPref;
    private AcceptedESR acceptedESR;

    @SuppressLint("MissingPermission")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        context = root.getContext();
        sharedPref = new SharedPref(context);

        Button btnESRAll = root.findViewById(R.id.btnESRall);
        Button btnESRAccepted = root.findViewById(R.id.btnESRaccepted);
        ListView listView =  root.findViewById(R.id.lv_ArticleTaskList);

        AdView mAdview =  root.findViewById(R.id.mAdview2);
        if (sharedPref.getBool("adSettingsStatusNotificationFragmentTopBannerAd")){

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


        mDataBaseRef = new FireBaseHelper(context);
        getESRList();

        btnESRAll.setOnClickListener(v -> {
            getESRList();
        });
        btnESRAccepted.setOnClickListener(v -> {
            startActivity(new Intent(context, AcceptedTask.class));
        });
        customAdapter = new CustomAdapterESRList(context, ESRModelArrayList);
        listView.setAdapter(customAdapter);

        return root;
    }
    private void getESRList() {
        ESRModelArrayList.clear();
        mDataBaseRef.showLoading();
        getEsrAcceptedListID();


    }
    private void getEsrAcceptedListID(){
        mDataBaseRef.getAcceptedESRRef().orderByChild("statusAndByUser").equalTo("Accepted"+sharedPref.getStr(Cons.USER_ID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                acceptedESR = null;
                if (snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                        acceptedESR = ds.getValue(AcceptedESR.class);
                    }
                }
                getESRListFromDataBase();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error: "+ error, Toast.LENGTH_SHORT).show();
                mDataBaseRef.hideLoading();
            }
        });
    }
    private void getESRListFromDataBase() {
        mDataBaseRef.getEsaleSawabRequestRef().orderByChild("status").equalTo(Cons.IN_PROGRESS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for (DataSnapshot ds:snapshot.getChildren()){
                                EsaleSawabRequest esaleSawabRequest = ds.getValue(EsaleSawabRequest.class);
                                assert esaleSawabRequest != null;

                                //agr total unit, finished unit k Equal hain to wo ad nahi hoga
                                if (!(Integer.parseInt(esaleSawabRequest.getAcceptedUnit()) >= Integer.parseInt(esaleSawabRequest.getTotalUnit()))){
                                    //agr acceptedESR ki Id Null nahi hai to Accepted ESR ko Exclude kro
                                    //warna Sab Include karo.
                                    if (acceptedESR != null){
                                        //Agr ESR Id Pehly say Accepted hai to Total ESR List mein Show na ho
                                        if (!esaleSawabRequest.getId().equals(acceptedESR.getEsrid())){
                                            ESRModelArrayList.add(esaleSawabRequest);

                                        }
                                    }
                                    else {
                                        ESRModelArrayList.add(esaleSawabRequest);
                                    }

                                }
                            }
                            customAdapter.notifyDataSetChanged();


                        } else {
                            Toast.makeText(context, "Sorry! not available any task", Toast.LENGTH_SHORT).show();

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
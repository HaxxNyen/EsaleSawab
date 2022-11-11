package com.nyen.User.esalesawab.UI.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.nyen.User.esalesawab.Adapters.CustomAdapterMarhomeenList;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.Marhoom;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.Utils.SharedPref;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class AddMarhoom extends AppCompatActivity {

    private EditText et_MarhoomName;
    private FireBaseHelper mDataBaseRef;
    private String Key;
    private String uId;
    private Marhoom marhoom;
    private final Context context = AddMarhoom.this;
    private final ArrayList<Marhoom> MarhomeenModelArrayList = new ArrayList<>();
    private CustomAdapterMarhomeenList customAdapter;
    private TextView etMarhoomStatus;
    private SharedPref sharedPref;
    private ConstraintLayout cvEmpty;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marhoom);

        mDataBaseRef = new FireBaseHelper(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        sharedPref = new SharedPref(context);



        cvEmpty = findViewById(R.id.emptyLayout);
        etMarhoomStatus =  findViewById(R.id.tv_Title_List);
        et_MarhoomName =  findViewById(R.id.et_MarhoomName);
        AdView mAdview =  findViewById(R.id.mAdview5);

        if (sharedPref.getBool("adSettingsStatusAddMarhoomTopBannerAd")){

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

        Button btnAddMarhoom =  findViewById(R.id.ivAddMarhoom);
        ListView listView = findViewById(R.id.lv_MarhoomList);

        uId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        getMarhoomList();




        btnAddMarhoom.setOnClickListener(view -> {


            mDataBaseRef.showLoading();
            String name = et_MarhoomName.getText().toString();
            if (name.length() <2) {
                et_MarhoomName.requestFocus();
                et_MarhoomName.setError("Please Input Name");
                mDataBaseRef.hideLoading();
                return;
            }




            Key = mDataBaseRef.getMarhomeenRef().push().getKey();
            marhoom = new Marhoom(Key, name,uId,name+"_"+uId,System.currentTimeMillis());

            mDataBaseRef.getMarhomeenRef().orderByChild("nameAndUserId").equalTo(name+"_"+uId).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Toast.makeText(context, "Name already exist.", Toast.LENGTH_SHORT).show();

                        mDataBaseRef.hideLoading();
                    } else {

                        mDataBaseRef.getMarhomeenRef().child(Key).setValue(marhoom).addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                Toast.makeText(context, "Successfully added", Toast.LENGTH_SHORT).show();
                                et_MarhoomName.setText("");
                                /*int lastIndex = MarhomeenModelArrayList.size();*/
                                MarhomeenModelArrayList.add(0,marhoom);

                                customAdapter.notifyDataSetChanged();
                                cvEmpty.setVisibility(View.GONE);
                                mDataBaseRef.hideLoading();


                            }

                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    mDataBaseRef.hideLoading();

                }
            });


        });

        customAdapter = new CustomAdapterMarhomeenList(context, MarhomeenModelArrayList);
        listView.setAdapter(customAdapter);

    }

    private void getMarhoomList() {


        mDataBaseRef.showLoading();
        mDataBaseRef.getMarhomeenRef().orderByChild("createdBy").equalTo(uId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                        Marhoom marhoom = ds.getValue(Marhoom.class);
                        assert marhoom != null;
                        MarhomeenModelArrayList.add(marhoom);

                    }

                    if(MarhomeenModelArrayList.size()>0){

                        Collections.reverse(MarhomeenModelArrayList);
                        customAdapter.notifyDataSetChanged();
                        mDataBaseRef.hideLoading();
                        cvEmpty.setVisibility(View.GONE);
                        etMarhoomStatus.setVisibility(View.VISIBLE);
                    } else {
                        upDateMarhoomList();

                    }


                }
                else {
                    upDateMarhoomList();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error: "+ error, Toast.LENGTH_SHORT).show();
                mDataBaseRef.hideLoading();

            }
        });





    }

    @SuppressLint("SetTextI18n")
    private void upDateMarhoomList() {
        cvEmpty.setVisibility(View.VISIBLE);
        etMarhoomStatus.setVisibility(View.GONE);
        mDataBaseRef.hideLoading();
    }
}
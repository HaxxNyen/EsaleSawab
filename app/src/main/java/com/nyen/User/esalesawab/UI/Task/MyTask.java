package com.nyen.User.esalesawab.UI.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nyen.User.esalesawab.Adapters.CustomAdapterMyTaskList;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.EsaleSawabRequest;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.Utils.Cons;
import com.nyen.User.esalesawab.Utils.SharedPref;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyTask extends AppCompatActivity {

    private final Context context = MyTask.this;
    private SharedPref sharedPref;
    private FireBaseHelper mDataBaseRef;
    private CustomAdapterMyTaskList customAdapter;
    private final ArrayList<EsaleSawabRequest> MyTaskModelArrayList = new ArrayList<>();

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task);
        sharedPref = new SharedPref(context);
        ListView listView =  findViewById(R.id.lv_ArticleMyTaskList);
        AdView mAdview =  findViewById(R.id.mAdview2);
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
        getMyTaskList();

        mDataBaseRef.ShowLog("Product is loading..");
        customAdapter = new CustomAdapterMyTaskList(context, MyTaskModelArrayList);
        mDataBaseRef.ShowLog("Adapter loaded..");
        listView.setAdapter(customAdapter);
        mDataBaseRef.ShowLog("List veiw loaded..");
    }



    private void getMyTaskList() {
        mDataBaseRef.showLoading();
        mDataBaseRef.getEsaleSawabRequestRef().orderByChild("userID").equalTo(sharedPref.getStr(Cons.USER_ID))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for (DataSnapshot ds:snapshot.getChildren()){
                                EsaleSawabRequest esaleSawabRequest = ds.getValue(EsaleSawabRequest.class);

                                assert esaleSawabRequest != null;
                                MyTaskModelArrayList.add(esaleSawabRequest);
                            }
                            customAdapter.notifyDataSetChanged();
                        } else {
                            new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                                    .setTitleText("Task is empty")
                                    .setContentText("Do you want to add new Request?")
                                    .setCancelText("No,Thanks!")
                                    .setConfirmText("Add ")
                                    .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                                    .setConfirmClickListener(sweetAlertDialog -> {
                                        startActivity(new Intent(context, AddArticles.class));
                                        sweetAlertDialog.dismissWithAnimation();
                                    })


                                    .show();

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
package com.nyen.User.esalesawab.UI.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nyen.User.esalesawab.Adapters.CustomAdapterArticleList;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.Articles;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.Utils.SharedPref;

import java.util.ArrayList;
import java.util.Collections;

public class AddArticles extends AppCompatActivity {

    private FireBaseHelper mDataBaseRef;
    private final Context context = AddArticles.this;
    private final ArrayList<Articles> ArticleModelArrayList = new ArrayList<>();
    private CustomAdapterArticleList customAdapter;
    private TextView etArticleStatus;
    private SharedPref sharedPref;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_articles);

        sharedPref = new SharedPref(context);
        mDataBaseRef = new FireBaseHelper(this);
        etArticleStatus = findViewById(R.id.tv_Title_List);
        ListView listView = findViewById(R.id.lv_ArticleList);

        AdView mAdview =  findViewById(R.id.mAdview3);

        if (sharedPref.getBool("adSettingsStatusAddArticleFragmentTopBannerAd")){

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

        getArticleList();
        customAdapter = new CustomAdapterArticleList(context, ArticleModelArrayList);
        listView.setAdapter(customAdapter);
    }
    private void getArticleList() {
        mDataBaseRef.showLoading();
        mDataBaseRef.getArticlesRef().orderByChild("aID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                        Articles articles = ds.getValue(Articles.class);
                        assert articles != null;
                        ArticleModelArrayList.add(articles);
                    }
                    if(ArticleModelArrayList.size()>0){
                        Collections.reverse(ArticleModelArrayList);
                        customAdapter.notifyDataSetChanged();
                        mDataBaseRef.hideLoading();
                    } else {
                        upDateArticleList();
                    }
                }
                else {
                    upDateArticleList();
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
    private void upDateArticleList() {
        etArticleStatus.setText("No Data Found");
        mDataBaseRef.hideLoading();
    }
}
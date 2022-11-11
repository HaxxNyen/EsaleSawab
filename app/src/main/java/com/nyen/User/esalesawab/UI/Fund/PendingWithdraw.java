package com.nyen.User.esalesawab.UI.Fund;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nyen.User.esalesawab.Adapters.CustomAdapterPendingWithdraw;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.WithdrawModel;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.Utils.Cons;
import com.nyen.User.esalesawab.Utils.SharedPref;

import java.util.ArrayList;
import java.util.Collections;

public class PendingWithdraw extends AppCompatActivity {

    private final Context context = PendingWithdraw.this;
    private CustomAdapterPendingWithdraw customAdapter;
    private final ArrayList<WithdrawModel> pendingWithdrawModelArrayList = new ArrayList<>();
    private ConstraintLayout cvEmpty;
    private FireBaseHelper mDataBaseRef;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_withdraw);


        mDataBaseRef = new FireBaseHelper(context);
        sharedPref = new SharedPref(context);

        mDataBaseRef.ShowToast("Pending Withdraw List");


        ListView listView = findViewById(R.id.lv);
        Button btnRefresh = findViewById(R.id.btnRefresh);
        cvEmpty = findViewById(R.id.emptyLayout);
        btnRefresh.setOnClickListener(view -> getProductList());

        customAdapter = new CustomAdapterPendingWithdraw(context, pendingWithdrawModelArrayList);
        listView.setAdapter(customAdapter);

        getProductList();

    }

    private void getProductList() {
        cvEmpty.setVisibility(View.GONE);
        pendingWithdrawModelArrayList.clear();
        mDataBaseRef.showLoading();
        mDataBaseRef.getWithdrawRef().orderByChild(Cons.STATUS_BY_USER).equalTo(Cons.PENDING+"_"+sharedPref.getStr(Cons.USER_ID))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                WithdrawModel withdrawModel = ds.getValue(WithdrawModel.class);
                                assert withdrawModel != null;
                                pendingWithdrawModelArrayList.add(withdrawModel);
                            }

                            Collections.reverse(pendingWithdrawModelArrayList);
                            customAdapter.notifyDataSetChanged();

                        } else {
                            cvEmpty.setVisibility(View.VISIBLE);


                        }
                        mDataBaseRef.hideLoading();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Error: " + error, Toast.LENGTH_SHORT).show();
                        mDataBaseRef.ShowLog("Error On Getting Product List: " + error);
                        mDataBaseRef.hideLoading();

                    }
                });
    }
}
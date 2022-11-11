package com.nyen.User.esalesawab.Fragments.LocalStore;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nyen.User.esalesawab.Adapters.CustomAdapterProductTrx;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.ProductTrx;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.Utils.Cons;
import com.nyen.User.esalesawab.Utils.SharedPref;

import java.util.ArrayList;
import java.util.Collections;


public class PurchaseHistoryLocal extends Fragment {

    private Context context;
    private ListView listView;
    private CustomAdapterProductTrx customAdapter;
    private final ArrayList<ProductTrx> productTrxModelArrayList = new ArrayList<>();
    private ConstraintLayout cvEmpty;
    private FireBaseHelper mDataBaseRef;
    private SharedPref sharedPref;


    public PurchaseHistoryLocal() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_history_local, container, false);
        context = view.getContext();
        mDataBaseRef = new FireBaseHelper(context);
        sharedPref = new SharedPref(context);

        mDataBaseRef.ShowToast("Purchase History");


        listView = view.findViewById(R.id.lv);
        Button btnRefresh = view.findViewById(R.id.btnRefresh);
        cvEmpty = view.findViewById(R.id.emptyLayout);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getProductList();
            }
        });

        customAdapter = new CustomAdapterProductTrx(context, productTrxModelArrayList);
        listView.setAdapter(customAdapter);

        getProductList();

        return view;
    }

    //TODO change pending purchase by user and pending status.
    private void getProductList() {
        productTrxModelArrayList.clear();
        mDataBaseRef.showLoading();
        mDataBaseRef.getLocalProductTrxRef().orderByChild("byUserID").equalTo(sharedPref.getStr(Cons.USER_ID))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ProductTrx productTrx = ds.getValue(ProductTrx.class);
                                assert productTrx != null;
                                productTrxModelArrayList.add(productTrx);
                            }

                            Collections.reverse(productTrxModelArrayList);
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
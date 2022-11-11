package com.nyen.User.esalesawab.Fragments.LocalStore;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nyen.User.esalesawab.Adapters.CustomAdapterProduct;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.Product;
import com.nyen.User.esalesawab.R;

import java.util.ArrayList;


public class LocalStore extends Fragment {

    private Context context;
    private CustomAdapterProduct customAdapter;
    private final ArrayList<Product> productModelArrayList  = new ArrayList<>();
    private ConstraintLayout cvEmpty;

    private FireBaseHelper mDataBaseRef;


    public LocalStore() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_store, container, false);
        context = view.getContext();
        mDataBaseRef = new FireBaseHelper(context);


        ListView listView = view.findViewById(R.id.lv);
        cvEmpty = view.findViewById(R.id.emptyLayout);

        customAdapter = new CustomAdapterProduct(context,productModelArrayList);
        listView.setAdapter(customAdapter);

        getProductList();

        return view;
    }

    private void getProductList() {
        productModelArrayList.clear();
        mDataBaseRef.showLoading();
        mDataBaseRef.getLocalProductRef().orderByChild("id")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for (DataSnapshot ds:snapshot.getChildren()){
                                Product product = ds.getValue(Product.class);
                                assert product != null;
                                productModelArrayList.add(product);
                            }

                            customAdapter.notifyDataSetChanged();

                        } else {
                            cvEmpty.setVisibility(View.VISIBLE);


                        }
                        mDataBaseRef.hideLoading();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Error: "+ error, Toast.LENGTH_SHORT).show();
                        mDataBaseRef.ShowLog("Error On Getting Product List: "+ error);
                        mDataBaseRef.hideLoading();

                    }
                });
    }
}
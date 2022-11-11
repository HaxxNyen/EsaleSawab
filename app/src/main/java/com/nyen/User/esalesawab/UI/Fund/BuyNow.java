package com.nyen.User.esalesawab.UI.Fund;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Help.NeedHelp;
import com.nyen.User.esalesawab.Model.Product;
import com.nyen.User.esalesawab.Model.ProductTrx;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.Utils.Cons;
import com.nyen.User.esalesawab.Utils.SharedPref;

import java.util.ArrayList;
import java.util.List;

public class BuyNow extends AppCompatActivity {


    private ProductTrx productTrx;
    private TextView buyNowName, buyNowCoins, buyNowPrice, etBuyNowTrxID;
    private Button btnAdd;
    private FireBaseHelper mDataBaseRef;
    private final Context context = BuyNow.this;
    private String Key;
    private Product product;
    private SharedPref sharedPref;
    private Spinner mySpinner;
    List<String> walletNamesArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_now);
        Initial();
        showProductDetail();

        btnAdd.setOnClickListener(view -> {

            if (ValidateField()){
                addProduct();

            }


        });


    }

    private void Initial() {

        buyNowName = findViewById(R.id.buyNowName);
        buyNowCoins = findViewById(R.id.buyNowCoins);
        buyNowPrice = findViewById(R.id.buyNowPrice);
        btnAdd = findViewById(R.id.btnAddRequestCoin);
        mySpinner = findViewById(R.id.spinnerWalletName);
        etBuyNowTrxID = findViewById(R.id.etBuyNowTrxID);
        TextView btnHelp = findViewById(R.id.btnHelp);

        btnHelp.setOnClickListener(view -> {
            Intent intent = new Intent(context, NeedHelp.class);
            startActivity(intent);
            finish();
        });


        mDataBaseRef = new FireBaseHelper(context);
        sharedPref = new SharedPref(context);

        walletNamesArray.add("MCB Bank");
        walletNamesArray.add("Easy Paisa");

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, walletNamesArray);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

    }


    @SuppressLint("SetTextI18n")
    private void showProductDetail() {
        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("id");

        buyNowName.setText(product.getName());
        buyNowCoins.setText(String.valueOf(product.getCoin()));
        String price = String.valueOf(product.getPrice());
        buyNowPrice.setText("Rs: "+price);
    }

    private boolean ValidateField() {


        if (etBuyNowTrxID.getText().toString().length() <3) {
            etBuyNowTrxID.requestFocus();
            etBuyNowTrxID.setError("Please Input Transaction Id");
            return false;
        }
        return true;
    }

    private void addProduct() {

        mDataBaseRef.showLoading();
        btnAdd.setEnabled(false);
        Key = mDataBaseRef.getLocalProductTrxRef().push().getKey();
        String BuyNowName = mySpinner.getSelectedItem().toString();
        String BuyNowTrxID = etBuyNowTrxID.getText().toString();
        productTrx = new ProductTrx(Key,product.getName(),product.getCoin(),product.getPrice(),sharedPref.getStr(Cons.USER_ID),BuyNowName,BuyNowTrxID,"Pending",System.currentTimeMillis());

        mDataBaseRef.getLocalProductTrxRef().orderByChild("trxID").equalTo(BuyNowTrxID).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Toast.makeText(context, "Transaction already exist.", Toast.LENGTH_LONG).show();
                    mDataBaseRef.hideLoading();
                } else {

                    mDataBaseRef.getLocalProductTrxRef().child(Key).setValue(productTrx).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Successfully added", Toast.LENGTH_SHORT).show();
                            mCleanField();
                            onBackPressed();

                        }

                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mDataBaseRef.hideLoading();

            }
        });

    }



    private void mCleanField() {
        etBuyNowTrxID.setText("");
        btnAdd.setEnabled(true);
        mDataBaseRef.hideLoading();

    }
}
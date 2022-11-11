package com.nyen.User.esalesawab.UI.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.Articles;
import com.nyen.User.esalesawab.Model.EsaleSawabRequest;
import com.nyen.User.esalesawab.Model.Marhoom;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.UI.Fund.Store;
import com.nyen.User.esalesawab.Utils.Cons;
import com.nyen.User.esalesawab.Utils.SharedPref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class AddEsaleSawabRequest extends AppCompatActivity {

    private final Context context = AddEsaleSawabRequest.this;
    private EditText etQuantity;
    private TextView requestHadya;
    private FireBaseHelper mDataBaseRef;
    private String Key;
    private EsaleSawabRequest esaleSawabRequest;
    private String uId;
    private int num;
    private SharedPref sharedPref;
    List<String> marhoomNamesArray = new ArrayList<>();
    private Spinner mySpinner;
    private boolean isSpinner = false;
    private EditText mRequestName;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_esale_sawab_request);

        mDataBaseRef = new FireBaseHelper(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        sharedPref = new SharedPref(context);
        uId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        TextView requestName = findViewById(R.id.tvRequestName);
        requestHadya = findViewById(R.id.tvRequestHadya);
        etQuantity = findViewById(R.id.etRequestQuantity);
        mRequestName = findViewById(R.id.etRequestName);
        Button btnAddRequest = findViewById(R.id.btnAddRequest);
        CheckBox checkBoxMarhoom = findViewById(R.id.cBMarhoom);
        TextInputLayout textInputLayout3 = findViewById(R.id.textInputLayout3);
        mySpinner = findViewById(R.id.spinner_carrier);


        checkBoxMarhoom.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                if (marhoomNamesArray.size()>0){
                    textInputLayout3.setVisibility(View.GONE);
                    mySpinner.setVisibility(View.VISIBLE);
                    isSpinner = true;
                }
                else {
                    isSpinner = false;
                    Toast.makeText(context, "Marhoom List is empty, Add new one.", Toast.LENGTH_SHORT).show();
                }

            }
            else {
                isSpinner = false;
                textInputLayout3.setVisibility(View.VISIBLE);
                mySpinner.setVisibility(View.GONE);

            }
        });

        addMarhoomToSpinner();





        AdView mAdview = findViewById(R.id.mAdview4);

        if (sharedPref.getBool("adSettingsStatusAddESRTopBannerAd")) {

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

        } else {
            mAdview.setVisibility(View.GONE);
        }


        Intent intent = getIntent();
        Articles articles = (Articles) intent.getSerializableExtra("id");


        requestName.setText(articles.aName);
        requestHadya.setText(articles.aHadya);


        sharedPref = new SharedPref(context);


        btnAddRequest.setOnClickListener(v -> {

            btnAddRequest.setEnabled(false);
            String quantity = etQuantity.getText().toString();
            if (quantity.length() < 1) {
                etQuantity.requestFocus();
                etQuantity.setError("Please Input Quantity");
                btnAddRequest.setEnabled(true);
                return;
            }
            if (Integer.parseInt(quantity) < 1) {
                etQuantity.requestFocus();
                etQuantity.setError("Please Input minimum quantity 1");

                btnAddRequest.setEnabled(true);
                return;
            }
            String MarhoomName;
            if (isSpinner){
                MarhoomName = mySpinner.getSelectedItem().toString();

            }else {

                MarhoomName = mRequestName.getText().toString();
                if (MarhoomName.length() < 3){

                    mRequestName.requestFocus();
                    mRequestName.setError("Please Input valid name");
                    btnAddRequest.setEnabled(true);
                    return;
                }

            }



            if (num > sharedPref.getInt(Cons.USER_COINS)) {
                new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText(Cons.INSUFFICIENT_FUND)
                        .setContentText(Cons.INSUFFICIENT_FUND_DESCRIPTION)
                        .setCancelText("No,Thanks!")
                        .setConfirmText(Cons.BUY_NOW)
                        .setCustomImage(R.drawable.ic_dollar)

                        .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                        .setConfirmClickListener(sweetAlertDialog ->{
                            sweetAlertDialog.dismissWithAnimation();

                            Intent goToStore = new Intent(context, Store.class);
                            startActivity(goToStore);
                            finish();

                        })


                        .show();
                btnAddRequest.setEnabled(true);

                return;

            }

            Key = mDataBaseRef.getEsaleSawabRequestRef().push().getKey();
            esaleSawabRequest = new EsaleSawabRequest(Key, uId, articles.aName, articles.aID, articles.aHadya, "0", quantity, "0",isSpinner,MarhoomName, Cons.IN_PROGRESS, System.currentTimeMillis());

            Map<String, Object> childUpdate = new HashMap<>();
            childUpdate.put("/EsaleSawabRequest/"+Key, esaleSawabRequest);
            mDataBaseRef.xyz("Request for " + articles.aName, "Request for " + articles.aName, num,false, childUpdate, "Esal e Sawab request added.");
            btnAddRequest.setEnabled(true);
            hideKeyboard(AddEsaleSawabRequest.this);


        });

        etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (etQuantity.getText().toString().length() > 0) {
                    num = Integer.parseInt(etQuantity.getText().toString()) * Integer.parseInt(articles.aHadya);

                    requestHadya.setText(String.valueOf(num));
                } else {
                    requestHadya.setText("0");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void addMarhoomToSpinner() {
        mDataBaseRef.showLoading();
        mDataBaseRef.getMarhomeenRef().orderByChild("createdBy").equalTo(sharedPref.getStr(Cons.USER_ID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for (DataSnapshot ds:snapshot.getChildren()) {
                        Marhoom marhoom = ds.getValue(Marhoom.class);
                        if (marhoom != null) {
                            marhoomNamesArray.add(marhoom.getmName());
                        }

                    }
                }

                if (marhoomNamesArray.size()>0) {
                    ArrayAdapter<String> myAdapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_list_item_1, marhoomNamesArray);
                    myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mySpinner.setAdapter(myAdapter);
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

    private void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
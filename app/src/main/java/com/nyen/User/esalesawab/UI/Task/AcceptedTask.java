package com.nyen.User.esalesawab.UI.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.R;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AcceptedTask extends AppCompatActivity {

    private TextView ESRListModelTitle, tvNote;
    private TextView tvESRCoins;
    private TextView tvESRTotalAndFinishedUnit;
    private ProgressBar EsrProgressBar;
    private SharedPref sharedPref;
    private FireBaseHelper mDataBaseRef;
    private final Context context = AcceptedTask.this;
    private CardView cvLayout;
    private String finished;
    private String coins;
    private String name;
    private String ESRidForTrx;
    private String getESRAcceptedId;
    private ConstraintLayout cvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_task);

        ESRListModelTitle = findViewById(R.id.ESRListModelTtile);
        tvNote = findViewById(R.id.tvNote);
        tvESRCoins = findViewById(R.id.tvESRCoins);
        tvESRTotalAndFinishedUnit = findViewById(R.id.tvESRTotalandFinishedUnit);
        EsrProgressBar = findViewById(R.id.ESRprogressBar);
        Button btnFinishedEsr = findViewById(R.id.btnAcceptedEsr);
        cvLayout = findViewById(R.id.cvLayout);
        cvEmpty = findViewById(R.id.emptyLayout);

        sharedPref = new SharedPref(context);
        mDataBaseRef = new FireBaseHelper(context);


        getEsrAcceptedList();
        btnFinishedEsr.setOnClickListener(view -> new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Disclaimer")
                .setContentText("میں حلفاً اقرار کرتا/کرتی ہوں کہ مَیں نے جو کچھ پڑھا ہے وہ صحیح ہے اور آخرت میں، میں خود اس کا/کی جواب دے ہوں گا/گی")
                .setCancelText("No,Thanks!")
                .setConfirmText("I Admit")
                .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                .setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    int finishedUnit = Integer.parseInt(finished) + 1;

                    Map<String, Object> childUpdate = new HashMap<>();
                    childUpdate.put("/EsaleSawabRequest/"+ESRidForTrx+"/finishedUnit", String.valueOf(finishedUnit));
                    childUpdate.put("/AcceptedESR/"+getESRAcceptedId+"/statusAndByUser", "finished" + sharedPref.getStr(Cons.USER_ID));
                    childUpdate.put("/AcceptedESR/"+getESRAcceptedId+"/status", "finished");

                    mDataBaseRef.xyz("Earn for reciting: " + name,
                            sharedPref.getStr(Cons.USER_NAME) + " Recited " + name,Integer.parseInt(coins), true, childUpdate,"Request complete");
                    cvLayout.setVisibility(View.GONE);
                })
                .show());
    }


    private void getEsrAcceptedList() {
        mDataBaseRef.showLoading();
        mDataBaseRef.getAcceptedESRRef().orderByChild("statusAndByUser").equalTo("Accepted" + sharedPref.getStr(Cons.USER_ID)).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                AcceptedESR acceptedESR = null;
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        acceptedESR = ds.getValue(AcceptedESR.class);
                    }
                }
                if (acceptedESR != null) {
                    getESRAcceptedId = acceptedESR.getaID();
                    getESRListFromDataBase(acceptedESR.getEsrid());
                } else {
                    mDataBaseRef.hideLoading();
                    cvEmpty.setVisibility(View.VISIBLE);
                    cvLayout.setVisibility(View.GONE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error: " + error, Toast.LENGTH_SHORT).show();
                mDataBaseRef.hideLoading();
            }
        });
    }


    private void getESRListFromDataBase(String ESRid) {

        mDataBaseRef.getEsaleSawabRequestRef().orderByChild("id").equalTo(ESRid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                EsaleSawabRequest esaleSawabRequest = ds.getValue(EsaleSawabRequest.class);
                                assert esaleSawabRequest != null;
                                if (!(Integer.parseInt(esaleSawabRequest.getFinishedUnit()) >= Integer.parseInt(esaleSawabRequest.getTotalUnit()))) {
                                    cvLayout.setVisibility(View.VISIBLE);
                                    cvEmpty.setVisibility(View.GONE);

                                    finished = esaleSawabRequest.getFinishedUnit();
                                    coins = esaleSawabRequest.getReward();
                                    ESRidForTrx = esaleSawabRequest.getId();
                                    name = esaleSawabRequest.getEsrname();
                                    ESRListModelTitle.setText(name);
                                    EsrProgressBar.setMax(Integer.parseInt(esaleSawabRequest.getTotalUnit()));
                                    tvESRTotalAndFinishedUnit.setText(esaleSawabRequest.getFinishedUnit() + "/" + esaleSawabRequest.getTotalUnit());
                                    EsrProgressBar.setProgress(Integer.parseInt(esaleSawabRequest.getFinishedUnit()));
                                    tvESRCoins.setText(coins);
                                    String note = "Please give sawab to ";
                                    if (esaleSawabRequest.isMarhoom()) {
                                        note = "Please give Esal e sawab to Marhoom : \n";
                                    }
                                    note = note + esaleSawabRequest.getMarhoomName();
                                    tvNote.setText(note);


                                }

                            }

                        } else {

                            cvEmpty.setVisibility(View.VISIBLE);
                            cvLayout.setVisibility(View.GONE);
                        }
                        mDataBaseRef.hideLoading();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Error: " + error, Toast.LENGTH_SHORT).show();
                        mDataBaseRef.hideLoading();

                    }
                });

    }
}
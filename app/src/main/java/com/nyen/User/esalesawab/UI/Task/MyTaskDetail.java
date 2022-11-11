package com.nyen.User.esalesawab.UI.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nyen.User.esalesawab.Adapters.CustomAdapterMyTaskReciterNameList;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.AcceptedESR;
import com.nyen.User.esalesawab.Model.EsaleSawabRequest;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.Utils.SharedPref;

import java.util.ArrayList;

public class MyTaskDetail extends AppCompatActivity {

    private final Context context = MyTaskDetail.this;
    private int total, mAcceptedUnit, finished;
    private SharedPref sharedPref;
    private FireBaseHelper mDataBaseRef;
    private CustomAdapterMyTaskReciterNameList customAdapter;
    private final ArrayList<AcceptedESR> MyTaskReciterNameModelArrayList = new ArrayList<>();
    private EsaleSawabRequest esaleSawabRequest;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task_detail);

        TextView MyTaskListModelTitle = (TextView) findViewById(R.id.MyTaskListModelTtile);
        TextView MyTaskListModelTaskStatusText = (TextView) findViewById(R.id.MyTaskListModelTaskStatusText);
        TextView tvESRTotalandFinishedUnit = (TextView) findViewById(R.id.tvESRTotalandFinishedUnit);
        TextView tvESRCoins = (TextView) findViewById(R.id.tvESRCoins);
        ProgressBar EsrProgressBar = (ProgressBar) findViewById(R.id.ESRprogressBar);
        ImageView MyTaskListModelTaskStatusIcon = (ImageView) findViewById(R.id.MyTaskListModelTaskStatusIcon);
        ListView listView =  findViewById(R.id.lv_ArticleMyTaskList);



        Intent intent = getIntent();
        esaleSawabRequest = (EsaleSawabRequest) intent.getSerializableExtra("id");
        MyTaskListModelTitle.setText(esaleSawabRequest.getEsrname());


        total = Integer.parseInt(esaleSawabRequest.getTotalUnit());
        mAcceptedUnit = Integer.parseInt(esaleSawabRequest.getAcceptedUnit());
        finished = Integer.parseInt(esaleSawabRequest.getFinishedUnit());
        if (isUnitFinished()){
            MyTaskListModelTaskStatusText.setText("Completed");
            MyTaskListModelTaskStatusIcon.setImageResource(R.drawable.outline_check_circle_24);
        }
        EsrProgressBar.setMax(Integer.parseInt(String.valueOf(total)));
        tvESRTotalandFinishedUnit.setText(finished+"/"+total);
        EsrProgressBar.setProgress(Integer.parseInt(String.valueOf(finished)));
        String coins = esaleSawabRequest.getReward();
        tvESRCoins.setText(coins);

        mDataBaseRef = new FireBaseHelper(context);
        getMyTaskListReciterName();

        customAdapter = new CustomAdapterMyTaskReciterNameList(context, MyTaskReciterNameModelArrayList);
        listView.setAdapter(customAdapter);



    }

    private void getMyTaskListReciterName() {
        mDataBaseRef.showLoading();
        mDataBaseRef.getAcceptedESRRef().orderByChild("esrid").equalTo(esaleSawabRequest.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for (DataSnapshot ds:snapshot.getChildren()){
                                AcceptedESR acceptedESR = ds.getValue(AcceptedESR.class);

                                assert acceptedESR != null;
                                MyTaskReciterNameModelArrayList.add(acceptedESR);
                            }
                            customAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "List is empty", Toast.LENGTH_SHORT).show();

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

    private boolean isUnitFinished() {
        return finished >= total ;
    }
}
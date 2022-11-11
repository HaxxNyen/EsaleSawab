package com.nyen.User.esalesawab.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.AcceptedESR;
import com.nyen.User.esalesawab.Model.EsaleSawabRequest;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.Utils.Cons;
import com.nyen.User.esalesawab.Utils.SharedPref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class CustomAdapterESRList extends BaseAdapter {

    private final Context context;
    private final ArrayList<EsaleSawabRequest> esaleSawabRequestModelArrayList;

    private FireBaseHelper mDataBaseRef;
    private SharedPref sharedPref;


    public CustomAdapterESRList(Context context, ArrayList<EsaleSawabRequest> ESRModelArrayList) {
        this.context = context;
        this.esaleSawabRequestModelArrayList = ESRModelArrayList;

    }









    @Override
    public int getCount() {
        return esaleSawabRequestModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return esaleSawabRequestModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        CustomAdapterESRList.ViewHolder holder;
        mDataBaseRef = new FireBaseHelper(context);
        sharedPref = new SharedPref(context);

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.request_modellist, null, true);

            holder.ESRListModelTitle = convertView.findViewById(R.id.ESRListModelTtile);
            holder.tvESRCoins = convertView.findViewById(R.id.tvESRCoins);
            holder.tvESRTotalAndFinishedUnit = convertView.findViewById(R.id.tvESRTotalandFinishedUnit);
            holder.EsrProgressBar = convertView.findViewById(R.id.ESRprogressBar);
            holder.btnAcceptedEsr = convertView.findViewById(R.id.btnAcceptedEsr);
            holder.MarhoomListModelTaskAcceptedList = convertView.findViewById(R.id.MarhoomListModelTaskAcceptedList);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (CustomAdapterESRList.ViewHolder)convertView.getTag();
        }
        if (esaleSawabRequestModelArrayList.size()>0) {
            String name = esaleSawabRequestModelArrayList.get(position).getEsrname();
            holder.ESRListModelTitle.setText(name);
            String coins = esaleSawabRequestModelArrayList.get(position).getReward();
            holder.tvESRCoins.setText(coins);
            String total = esaleSawabRequestModelArrayList.get(position).getTotalUnit();
            String mAcceptedUnit = esaleSawabRequestModelArrayList.get(position).getAcceptedUnit();
            holder.EsrProgressBar.setMax(Integer.parseInt(total));

                holder.tvESRTotalAndFinishedUnit.setText(mAcceptedUnit + "/" + total);
                holder.EsrProgressBar.setProgress(Integer.parseInt(mAcceptedUnit));
                holder.MarhoomListModelTaskAcceptedList.setText("Task Accepted");
                holder.btnAcceptedEsr.setText("Accept ");

        }
      holder.btnAcceptedEsr.setOnClickListener(v -> AddAcceptedESR(esaleSawabRequestModelArrayList.get(position).getId(), position, Integer.parseInt(esaleSawabRequestModelArrayList.get(position).getAcceptedUnit()),esaleSawabRequestModelArrayList.get(position).getTotalUnit()));
        return convertView;
    }



    public void AddAcceptedESR(String ESRID, int position, int acceptedUnit, String total){

        mDataBaseRef.ShowLog("adAccepted mein "+ acceptedUnit);
        mDataBaseRef.showLoading();
        sharedPref = new SharedPref(context);
        mDataBaseRef.getAcceptedESRRef().orderByChild("statusAndByUser").equalTo("Accepted"+sharedPref.getStr(Cons.USER_ID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    addAcceptedESR(ESRID, position, acceptedUnit, total);

                }else {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Your have already a pending task, please complete it first.")
                            .show();
                }
                mDataBaseRef.hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error: "+error, Toast.LENGTH_SHORT).show();
                mDataBaseRef.hideLoading();

            }
        });
    }

    private void addAcceptedESR(String ESRID, int position, int acceptedUnit, String total) {
        Map<String, Object> childUpdateForAcceptedESR = new HashMap<>();

        mDataBaseRef.ShowLog("adAccepted mein ESR mein "+ acceptedUnit);
        if (!(acceptedUnit >= Integer.parseInt(total))){
            String key = mDataBaseRef.getAcceptedESRRef().push().getKey();

            AcceptedESR acceptedESR1 = new AcceptedESR(key,ESRID,"Accepted",sharedPref.getStr(Cons.USER_ID),
                    sharedPref.getStr(Cons.USER_NAME),"Accepted"+sharedPref.getStr(Cons.USER_ID),System.currentTimeMillis());

            assert key != null;
            mDataBaseRef.ShowLog(String.valueOf(acceptedUnit));
            int finalAcceptedUnit = acceptedUnit + 1;
            childUpdateForAcceptedESR.put("/AcceptedESR/"+key , acceptedESR1);
            childUpdateForAcceptedESR.put("/EsaleSawabRequest/"+ESRID+"/acceptedUnit" , String.valueOf(finalAcceptedUnit));
            mDataBaseRef.setChildrenQuery(childUpdateForAcceptedESR, "Added Successfully.");
            mDataBaseRef.hideLoading();

            //TODO:Log Remove after final Output
            mDataBaseRef.ShowLog(String.valueOf(finalAcceptedUnit));
            esaleSawabRequestModelArrayList.remove(position);
            notifyDataSetChanged();

        } else {
            Toast.makeText(context, "Sorry! already task assign to many users.", Toast.LENGTH_SHORT).show();

        }

    }



    private static class ViewHolder {

        public TextView MarhoomListModelTaskAcceptedList;
        protected Button btnAcceptedEsr;
        protected TextView ESRListModelTitle, tvESRCoins, tvESRTotalAndFinishedUnit;
        protected ProgressBar EsrProgressBar;

    }

}
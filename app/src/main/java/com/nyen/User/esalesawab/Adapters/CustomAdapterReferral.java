package com.nyen.User.esalesawab.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.Referral;
import com.nyen.User.esalesawab.R;


import java.util.ArrayList;

public class CustomAdapterReferral extends BaseAdapter {

    private final Context context;
    private final ArrayList<Referral> referralArrayList;
    private FireBaseHelper mDataBaseRef;


    public CustomAdapterReferral(Context context, ArrayList<Referral> referralArrayList) {

        this.context = context;
        this.referralArrayList = referralArrayList;
    }


    @Override
    public int getCount() {
        return referralArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return referralArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.referral_list_model, null, true);

            holder.referral_list_model_number = convertView.findViewById(R.id.referral_list_model_number);
            holder.referral_list_model_status = convertView.findViewById(R.id.referral_list_model_status);
            holder.btnRemindAgain = convertView.findViewById(R.id.btnRemind);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        holder.referral_list_model_number.setText(referralArrayList.get(position).getPhone());
        String status =  referralArrayList.get(position).getStatus();
        holder.referral_list_model_status.setText(status);

        if(status.equals("Pending")){
            holder.btnRemindAgain.setVisibility(View.VISIBLE);
        }

        if(status.equals("Registered")){
            holder.referral_list_model_status.setTextColor(Color.BLUE);
            holder.btnRemindAgain.setVisibility(View.GONE);
        }

        if(status.equals("Deposited")) {
            holder.referral_list_model_status.setTextColor(context.getResources().getColor(R.color.Dr_Color));
        }
        mDataBaseRef = new FireBaseHelper(context);
        holder.btnRemindAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return convertView;
    }

    private static class ViewHolder {

        protected TextView referral_list_model_number, referral_list_model_status ;
        protected Button btnRemindAgain;

    }

}
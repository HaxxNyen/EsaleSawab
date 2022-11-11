package com.nyen.User.esalesawab.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.FundTransfer;
import com.nyen.User.esalesawab.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CustomAdapterViewPointHistoryList extends BaseAdapter {

    private final Context context;
    private final ArrayList<FundTransfer> ViewPointHistoryModelArrayList;

    private FireBaseHelper mDataBaseRef;


    public CustomAdapterViewPointHistoryList(Context context, ArrayList<FundTransfer> viewPointHistoryModelArrayList) {

        this.context = context;
        this.ViewPointHistoryModelArrayList = viewPointHistoryModelArrayList;
    }



    @Override
    public int getCount() {
        return ViewPointHistoryModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return ViewPointHistoryModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"InflateParams", "ResourceAsColor"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CustomAdapterViewPointHistoryList.ViewHolder holder;
        mDataBaseRef = new FireBaseHelper(context);

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_points_history_model_list, null, true);

            holder.ViewPointsHistoryDescription = (TextView) convertView.findViewById(R.id.ViewPointsHistoryDescription);
            holder.ViewPointsHistoryTime = (TextView) convertView.findViewById(R.id.ViewPointsHistoryTime);
            holder.ViewPointsHistoryPoints = (TextView) convertView.findViewById(R.id.ViewPointsHistoryPoints);


            convertView.setTag(holder);
        }else {

            holder = (CustomAdapterViewPointHistoryList.ViewHolder)convertView.getTag();
        }

        holder.ViewPointsHistoryDescription.setText(ViewPointHistoryModelArrayList.get(position).getDescription());

        Long currentTime = ViewPointHistoryModelArrayList.get(position).getTime();
        SimpleDateFormat simpleFormattime = new SimpleDateFormat("hh:mm:ss:a");
        SimpleDateFormat simpleFormatDate = new SimpleDateFormat("dd:MM:yyyy");
        Date date = new Date(currentTime);
        String mTime = simpleFormattime.format(date);
        String mDate = simpleFormatDate.format(date);
        holder.ViewPointsHistoryTime.setText(mDate+"  "+mTime);
        holder.ViewPointsHistoryPoints.setText(ViewPointHistoryModelArrayList.get(position).getCoins());
        if (ViewPointHistoryModelArrayList.get(position).getTrxType().equals("Dr")){
            holder.ViewPointsHistoryPoints.setTextColor(ContextCompat.getColor(context, R.color.Dr_Color));
        }
        else {
            holder.ViewPointsHistoryPoints.setTextColor(ContextCompat.getColor(context, R.color.Cr_Color));
        }




        return convertView;
    }

    private static class ViewHolder {

        protected TextView ViewPointsHistoryDescription, ViewPointsHistoryTime, ViewPointsHistoryPoints;

    }

}

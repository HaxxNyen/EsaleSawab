package com.nyen.User.esalesawab.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nyen.User.esalesawab.Model.WithdrawModel;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.Utils.Cons;
import com.nyen.User.esalesawab.Utils.DateAndTime;

import java.util.ArrayList;

public class CustomAdapterPendingWithdraw extends BaseAdapter {

    private final Context context;
    private final ArrayList<WithdrawModel> pendingWithdrawArrayList;
    private final DateAndTime dateAndTime ;


    public CustomAdapterPendingWithdraw(Context context, ArrayList<WithdrawModel> pendingWithdrawArrayList) {

        this.context = context;
        this.pendingWithdrawArrayList = pendingWithdrawArrayList;
        this.dateAndTime = new DateAndTime();
    }


    @Override
    public int getCount() {
        return pendingWithdrawArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return pendingWithdrawArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.pending_withdraw_list_model, null, true);

            holder.pendingWithdrawsCoins = convertView.findViewById(R.id.pendingWithdrawsCoins);
            holder.pendingWithdrawImage = convertView.findViewById(R.id.pendingWithdrawImage);
            holder.pendingWithdrawsStatus = convertView.findViewById(R.id.pendingWithdrawsStatus);
            holder.pendingWithdrawTrxTime = convertView.findViewById(R.id.pendingWithdrawTrxTime);
            holder.pendingWithdrawsStatusIcon = convertView.findViewById(R.id.pendingWithdrawsStatusIcon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.pendingWithdrawsCoins.setText(String.valueOf(pendingWithdrawArrayList.get(position).getCoins()));
        holder.pendingWithdrawsStatus.setText(pendingWithdrawArrayList.get(position).getStatus());

        if (pendingWithdrawArrayList.get(position).getStatus().equals(Cons.APPROVED)) {
            holder.pendingWithdrawsStatusIcon.setImageResource(R.drawable.outline_check_circle_24);
            holder.pendingWithdrawsStatus.setTextColor(context.getResources().getColor(R.color.Dr_Color));
        }
        long currentTime = pendingWithdrawArrayList.get(position).getTime();
        holder.pendingWithdrawTrxTime.setText(dateAndTime.getDateF(currentTime) + " " + dateAndTime.getTimeF(currentTime));

        return convertView;
    }

    private static class ViewHolder {


        protected TextView pendingWithdrawsCoins, pendingWithdrawsStatus, pendingWithdrawTrxTime;
        protected ImageView pendingWithdrawImage, pendingWithdrawsStatusIcon;

    }

}
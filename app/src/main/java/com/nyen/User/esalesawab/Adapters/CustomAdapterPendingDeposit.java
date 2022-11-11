package com.nyen.User.esalesawab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.FundTransfer;
import com.nyen.User.esalesawab.R;

import java.util.ArrayList;

/**
 * Created by Parsania Hardik on 26-Apr-17.
 */
public class CustomAdapterPendingDeposit extends BaseAdapter {

    private Context context;
    private ArrayList<FundTransfer> pendingDepositArrayList;
    private FireBaseHelper fireBaseHelper;


    public CustomAdapterPendingDeposit(Context context, ArrayList<FundTransfer> pendingDepositArrayList) {

        this.context = context;
        this.pendingDepositArrayList = pendingDepositArrayList;
    }


    @Override
    public int getCount() {
        return pendingDepositArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return pendingDepositArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.pendingdepositmodellist, null, true);

            holder.DepositListModelTtile = (TextView) convertView.findViewById(R.id.DepositListModelTtile);
            holder.DepositListModelAccount = (TextView) convertView.findViewById(R.id.DepositListModelAccount);
            holder.DepositListModelAmount = (TextView) convertView.findViewById(R.id.DepositListModelAmount);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        holder.DepositListModelTtile.setText("Cash Deposit");
        String amount = String.valueOf(pendingDepositArrayList.get(position).getCoins());
        holder.DepositListModelAmount.setText(amount);

        holder.DepositListModelAccount.setText(pendingDepositArrayList.get(position).getTrxType());
        String status =  pendingDepositArrayList.get(position).getStatus();
        /*holder.DepositListModelAccount.setText(status);


        if(status.equals("Deposited")) {
            holder.DepositListModelTtile.setTextColor(context.getResources().getColor(R.color.Status_deposit));
        }*/


        return convertView;
    }

    private class ViewHolder {

        protected TextView DepositListModelTtile, DepositListModelAccount, DepositListModelAmount ;

    }


}
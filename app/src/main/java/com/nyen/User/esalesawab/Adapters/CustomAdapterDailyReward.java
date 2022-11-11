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

import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.DailyRewardsModel;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.Utils.SharedPref;

import java.util.ArrayList;


public class CustomAdapterDailyReward extends BaseAdapter {

    private final Context context;
    private final ArrayList<DailyRewardsModel> DailyRewardsModelModelArrayList;

    private FireBaseHelper mDataBaseRef;
    private SharedPref sharedPref;





    public CustomAdapterDailyReward(Context context, ArrayList<DailyRewardsModel> dailyRewardsModelModelArrayList) {
        this.context = context;
        this.DailyRewardsModelModelArrayList = dailyRewardsModelModelArrayList;

    }



    @Override
    public int getCount() {
        return DailyRewardsModelModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return DailyRewardsModelModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        CustomAdapterDailyReward.ViewHolder holder;
        mDataBaseRef = new FireBaseHelper(context);
        sharedPref = new SharedPref(context);

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.daily_reward_model_list, null, true);

            holder.DailyRewardTitle = (TextView) convertView.findViewById(R.id.DailyRewardTitle);
            holder.DailyRewardCoins = (TextView) convertView.findViewById(R.id.DailyRewardCoins);
            holder.DailyRewardTotalAndFinishedUnit = (TextView) convertView.findViewById(R.id.DailyRewardTotalAndFinishedUnit);
            holder.DailyRewardCOuntDown = (TextView) convertView.findViewById(R.id.DailyRewardCOuntDown);
            holder.DailyRewardProgressBar = (ProgressBar) convertView.findViewById(R.id.DailyRewardprogressBar);
            holder.DailyRewardBtnClaim = (Button) convertView.findViewById(R.id.DailyRewardBtnClaim);


            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (CustomAdapterDailyReward.ViewHolder)convertView.getTag();
        }


        holder.DailyRewardTitle.setText(DailyRewardsModelModelArrayList.get(position).getName());
        holder.DailyRewardCoins.setText(String.valueOf(DailyRewardsModelModelArrayList.get(position).getCoins()));
        int mFinishedUnit = DailyRewardsModelModelArrayList.get(position).getFinishedUnits();
        int mTotalUnit = DailyRewardsModelModelArrayList.get(position).getTotalUnits();
        holder.DailyRewardTotalAndFinishedUnit.setText(mFinishedUnit+"/"+mTotalUnit);
        holder.DailyRewardProgressBar.setMax(mTotalUnit);
        holder.DailyRewardProgressBar.setProgress(mFinishedUnit);

        holder.DailyRewardBtnClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "position "+ DailyRewardsModelModelArrayList.get(position).getName()+ " Clicked", Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;
    }






    private static class ViewHolder {

        protected ProgressBar DailyRewardProgressBar;
        protected Button DailyRewardBtnClaim;
        protected TextView DailyRewardTitle, DailyRewardCoins, DailyRewardTotalAndFinishedUnit, DailyRewardCOuntDown;

    }

}
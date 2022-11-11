package com.nyen.User.esalesawab.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.EsaleSawabRequest;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.UI.Task.MyTaskDetail;

import java.util.ArrayList;


public class CustomAdapterMyTaskList extends BaseAdapter {

    private final Context context;
    private final ArrayList<EsaleSawabRequest> MyTaskModelArrayList;

    private int total;
    private int finished;
    private final FireBaseHelper mDataBaseRef;


    public CustomAdapterMyTaskList(Context context, ArrayList<EsaleSawabRequest> myTaskModelArrayList) {

        this.context = context;
        this.MyTaskModelArrayList = myTaskModelArrayList;
        mDataBaseRef = new FireBaseHelper(context);
    }



    @Override
    public int getCount() {
        mDataBaseRef.ShowLog("list size is "+ MyTaskModelArrayList.size());
        return MyTaskModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return MyTaskModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CustomAdapterMyTaskList.ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_task_model_list, null, true);

            holder.MyTaskListModelTitle = (TextView) convertView.findViewById(R.id.MyTaskListModelTtile);
            holder.MyTaskListModelTaskStatusText = (TextView) convertView.findViewById(R.id.MyTaskListModelTaskStatusText);
            holder.tvESRTotalAndFinishedUnit = (TextView) convertView.findViewById(R.id.tvESRTotalandFinishedUnit);
            holder.tvESRCoins = (TextView) convertView.findViewById(R.id.tvESRCoins);
            holder.EsrProgressBar = (ProgressBar) convertView.findViewById(R.id.ESRprogressBar);
            holder.MyTaskListModelTaskStatusIcon = (ImageView) convertView.findViewById(R.id.MyTaskListModelTaskStatusIcon);
            holder.btnViewReciter = (TextView) convertView.findViewById(R.id.btnViewReciter);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (CustomAdapterMyTaskList.ViewHolder)convertView.getTag();
        }

        String name = String.valueOf(MyTaskModelArrayList.get(position).getEsrname());
        total = Integer.parseInt(MyTaskModelArrayList.get(position).getTotalUnit());
        finished = Integer.parseInt(MyTaskModelArrayList.get(position).getFinishedUnit());
        holder.MyTaskListModelTitle.setText(name);
        if (isUnitFinished()){
            holder.MyTaskListModelTaskStatusText.setText("Completed");
            holder.MyTaskListModelTaskStatusIcon.setImageResource(R.drawable.outline_check_circle_24);
        }
        holder.EsrProgressBar.setMax(Integer.parseInt(String.valueOf(total)));
        holder.tvESRTotalAndFinishedUnit.setText(finished+"/"+total);
        holder.EsrProgressBar.setProgress(Integer.parseInt(String.valueOf(finished)));
        String coins = MyTaskModelArrayList.get(position).getReward();
        holder.tvESRCoins.setText(coins);


        holder.btnViewReciter.setOnClickListener(v -> {

            EsaleSawabRequest esaleSawabRequest = MyTaskModelArrayList.get(position);
            Intent intent = new Intent(context, MyTaskDetail.class);
            intent.putExtra("id", esaleSawabRequest);
            context.startActivity(intent);


        });


        return convertView;
    }

    private boolean isUnitFinished() {
        return finished >= total ;
    }

    private static class ViewHolder {

        protected TextView MyTaskListModelTitle, MyTaskListModelTaskStatusText, tvESRTotalAndFinishedUnit, tvESRCoins, btnViewReciter;
        protected ImageView  MyTaskListModelTaskStatusIcon;
        protected ProgressBar EsrProgressBar;

    }

}

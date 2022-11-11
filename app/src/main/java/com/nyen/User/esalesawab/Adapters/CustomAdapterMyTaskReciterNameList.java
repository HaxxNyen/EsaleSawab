package com.nyen.User.esalesawab.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.AcceptedESR;
import com.nyen.User.esalesawab.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CustomAdapterMyTaskReciterNameList extends BaseAdapter {

    private final Context context;
    private final ArrayList<AcceptedESR> MyTaskReciterNameModelArrayList;

    private FireBaseHelper mDataBaseRef;




    public CustomAdapterMyTaskReciterNameList(Context context, ArrayList<AcceptedESR> myTaskReciterNameModelArrayList) {

        this.context = context;
        this.MyTaskReciterNameModelArrayList = myTaskReciterNameModelArrayList;
    }



    @Override
    public int getCount() {
        return MyTaskReciterNameModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return MyTaskReciterNameModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CustomAdapterMyTaskReciterNameList.ViewHolder holder;
        mDataBaseRef = new FireBaseHelper(context);

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_task_receiter_name_model_list, null, true);

            holder.name = (TextView) convertView.findViewById(R.id.MyTaskReceiterName);
            holder.time = (TextView) convertView.findViewById(R.id.MyTaskReceiterTime);


            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (CustomAdapterMyTaskReciterNameList.ViewHolder)convertView.getTag();
        }

        String name = MyTaskReciterNameModelArrayList.get(position).getByUserName();
        Long time = MyTaskReciterNameModelArrayList.get(position).getTime();

        holder.name.setText(name);


        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleFormatTime = new SimpleDateFormat("hh:mm:ss:a");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleFormatDate = new SimpleDateFormat("dd:MM:yyyy");
        Date date = new Date(time);
        String mTime = simpleFormatTime.format(date);
        String mDate = simpleFormatDate.format(date);

        holder.time.setText(mDate+" "+mTime);



        return convertView;
    }


    private static class ViewHolder {

        protected TextView name, time;

    }

}

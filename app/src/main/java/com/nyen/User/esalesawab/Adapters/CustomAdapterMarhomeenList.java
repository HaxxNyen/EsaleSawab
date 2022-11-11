package com.nyen.User.esalesawab.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.Marhoom;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.Utils.DateAndTime;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CustomAdapterMarhomeenList extends BaseAdapter {

    private final Context context;
    private final ArrayList<Marhoom> MarhomeenModelArrayList;

    private FireBaseHelper mDataBaseRef;


    public CustomAdapterMarhomeenList(Context context, ArrayList<Marhoom> MarhomeenModelArrayList) {

        this.context = context;
        this.MarhomeenModelArrayList = MarhomeenModelArrayList;
    }



    @Override
    public int getCount() {
        return MarhomeenModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return MarhomeenModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CustomAdapterMarhomeenList.ViewHolder holder;
        mDataBaseRef = new FireBaseHelper(context);

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.marhomeenmodellist, null, true);

            holder.MarhomeenListModelTitle =  convertView.findViewById(R.id.MarhoomListModelTtile);
            holder.MarhomeenListModelTime =  convertView.findViewById(R.id.MarhoomListModelTime);
            holder.MarhomeenListModelDelete = convertView.findViewById(R.id.ivMarhoomDelete);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (CustomAdapterMarhomeenList.ViewHolder)convertView.getTag();
        }

        String name = String.valueOf(MarhomeenModelArrayList.get(position).getmName());
        holder.MarhomeenListModelTitle.setText(name);
        //Time Calculation
        Long currentTime = MarhomeenModelArrayList.get(position).getTime();

        holder.MarhomeenListModelTime.setText(DateAndTime.getDateF(currentTime) + " " + DateAndTime.getTimeF(currentTime));

        holder.MarhomeenListModelDelete.setOnClickListener(v -> new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Delete")
                .setContentText("Do you want to delete?")
                .setCancelText("No")
                .setConfirmText("Delete")
                .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                .setConfirmClickListener(sweetAlertDialog -> {
                    mDataBaseRef.DeleteMarhoom(context,MarhomeenModelArrayList.get(position).getmID());
                    MarhomeenModelArrayList.remove(position);
                    notifyDataSetChanged();
                    sweetAlertDialog.dismissWithAnimation();
                })
                .show());


        return convertView;
    }

    private static class ViewHolder {

        protected TextView MarhomeenListModelTitle, MarhomeenListModelTime ;
        protected ImageView  MarhomeenListModelDelete;

    }


}


package com.nyen.User.esalesawab.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.Model.ProductTrx;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.Utils.Cons;
import com.nyen.User.esalesawab.Utils.DateAndTime;
import com.nyen.User.esalesawab.Utils.SharedPref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomAdapterProductTrx extends BaseAdapter {

    private final Context context;
    private final ArrayList<ProductTrx> productTrxArrayList;
    private final DateAndTime DateAndTime = new DateAndTime();
    private final SharedPref sharedPref;
    private FireBaseHelper mDataBaseRef;


    public CustomAdapterProductTrx(Context context, ArrayList<ProductTrx> productArrayList) {

        this.context = context;
        this.productTrxArrayList = productArrayList;
        mDataBaseRef = new FireBaseHelper(context);
        sharedPref = new SharedPref(context);
    }


    @Override
    public int getCount() {
        return productTrxArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return productTrxArrayList.get(position);
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
            convertView = inflater.inflate(R.layout.product_trx_list_model, null, true);

            holder.productsName = convertView.findViewById(R.id.productsName);
            holder.productsCoins = convertView.findViewById(R.id.productsCoins);
            holder.productPrice = convertView.findViewById(R.id.productPrice);
            holder.productImage = convertView.findViewById(R.id.productImage);
            holder.productsStatus = convertView.findViewById(R.id.productsStatus);
            holder.productTrxTime = convertView.findViewById(R.id.productTrxTime);
            holder.productsStatusIcon = convertView.findViewById(R.id.productsStatusIcon);
            holder.btnProductTrxListClaim = convertView.findViewById(R.id.btnProductTrxListClaim);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.productsName.setText(String.valueOf(productTrxArrayList.get(position).getName()));
        int coin = productTrxArrayList.get(position).getCoin();
        holder.productsCoins.setText(String.valueOf(coin));
        String price = String.valueOf(productTrxArrayList.get(position).getPrice());
        holder.productPrice.setText("Rs: " + price);
        holder.productsStatus.setText(productTrxArrayList.get(position).getStatus());

        holder.productsStatusIcon.setVisibility(View.GONE);
        holder.productsStatus.setVisibility(View.GONE);
        holder.btnProductTrxListClaim.setVisibility(View.GONE);

        if (productTrxArrayList.get(position).getStatus().equals(Cons.PENDING)) {

            holder.productsStatusIcon.setVisibility(View.VISIBLE);
            holder.productsStatus.setVisibility(View.VISIBLE);

        }
        if (productTrxArrayList.get(position).getStatus().equals(Cons.APPROVED)) {
            holder.btnProductTrxListClaim.setVisibility(View.VISIBLE);
            holder.btnProductTrxListClaim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = productTrxArrayList.get(position).getId();

                    Map<String, Object> childUpdate = new HashMap<>();
                    childUpdate.put("/LocalProductTrx/"+id+"/status", Cons.COMPLETE);
                    mDataBaseRef.xyz("Coins purchased", "Coins purchased by "+ sharedPref.getStr(Cons.USER_PHONE), coin,true, childUpdate, "Coins purchased Successfully.");
                    productTrxArrayList.remove(position);
                    notifyDataSetChanged();

                }
            });

        }
        if (productTrxArrayList.get(position).getStatus().equals(Cons.COMPLETE)) {
            holder.productsStatusIcon.setVisibility(View.VISIBLE);
            holder.productsStatus.setVisibility(View.VISIBLE);

            holder.productsStatusIcon.setImageResource(R.drawable.outline_check_circle_24);
            holder.productsStatus.setTextColor(context.getResources().getColor(R.color.Dr_Color));

        }
        long currentTime = productTrxArrayList.get(position).getTime();
        holder.productTrxTime.setText(DateAndTime.getDateF(currentTime) + " " + DateAndTime.getTimeF(currentTime));

        return convertView;
    }

    private static class ViewHolder {


        protected TextView productsCoins, productPrice, productsName, productsStatus, productTrxTime;
        protected ImageView productImage, productsStatusIcon;
        protected Button btnProductTrxListClaim;

    }

}
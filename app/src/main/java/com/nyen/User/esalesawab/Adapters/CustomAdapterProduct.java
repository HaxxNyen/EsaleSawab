package com.nyen.User.esalesawab.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nyen.User.esalesawab.Model.Product;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.UI.Fund.BuyNow;

import java.util.ArrayList;

public class CustomAdapterProduct extends BaseAdapter {

    private final Context context;
    private final ArrayList<Product> productArrayList;


    public CustomAdapterProduct(Context context, ArrayList<Product> productArrayList) {

        this.context = context;
        this.productArrayList = productArrayList;
    }




    @Override
    public int getCount() {
        return productArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return productArrayList.get(position);
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
            convertView = inflater.inflate(R.layout.product_list_model, null, true);

            holder.productsName = convertView.findViewById(R.id.productsName);
            holder.productsCoins = convertView.findViewById(R.id.productsCoins);
            holder.productPrice = convertView.findViewById(R.id.productPrice);
            holder.productImage = convertView.findViewById(R.id.productImage);
            holder.btnBuy = convertView.findViewById(R.id.btnBuy);

            convertView.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.productsName.setText(String.valueOf(productArrayList.get(position).getName()));
        holder.productsCoins.setText(String.valueOf(productArrayList.get(position).getCoin()));
        String price = String.valueOf(productArrayList.get(position).getPrice());
        holder.productPrice.setText("Rs: " + price);
        holder.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product product = productArrayList.get(position);
                Intent intent = new Intent(context, BuyNow.class);
                intent.putExtra("id", product);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private static class ViewHolder {

        public Button btnBuy;
        protected TextView productsCoins, productPrice, productsName;
        protected ImageView productImage;

    }

}
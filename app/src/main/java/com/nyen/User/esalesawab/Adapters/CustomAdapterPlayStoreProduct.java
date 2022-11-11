package com.nyen.User.esalesawab.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.SkuDetails;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.R;

import java.util.List;


public class CustomAdapterPlayStoreProduct extends BaseAdapter {

    private final Context context;
    private final AppCompatActivity appCompatActivity;
    private final List<SkuDetails> skuDetailsList;
    private final BillingClient billingClient;
    private final FireBaseHelper mDataBaseRef;


    public CustomAdapterPlayStoreProduct(Context context, AppCompatActivity appCompatActivity, List<SkuDetails> skuDetailsList, BillingClient billingClient) {
        this.context = context;
        this.appCompatActivity = appCompatActivity;
        this.skuDetailsList = skuDetailsList;
        this.billingClient = billingClient;
        mDataBaseRef = new FireBaseHelper(context);
        mDataBaseRef.ShowLog("CustomAdapterProductFromPlayStore Init...");
    }

    @Override
    public int getCount() {
        mDataBaseRef.ShowLog("list size is "+ skuDetailsList.size());
        return skuDetailsList.size();
    }

    @Override
    public Object getItem(int position) {
        return skuDetailsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CustomAdapterPlayStoreProduct.ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.product_list_model, null, true);
            mDataBaseRef.ShowLog("View inflated");

            holder.productsName = convertView.findViewById(R.id.productsName);
            holder.productsCoins = convertView.findViewById(R.id.productsCoins);
            holder.productPrice = convertView.findViewById(R.id.productPrice);
            holder.productImage = convertView.findViewById(R.id.productImage);
            holder.btnBuy = convertView.findViewById(R.id.btnBuy);
            mDataBaseRef.ShowLog("field init");

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            mDataBaseRef.ShowLog("// the getTag returns the viewHolder object set as a tag to the view");
            holder = (CustomAdapterPlayStoreProduct.ViewHolder)convertView.getTag();
        }




        String name = skuDetailsList.get(position).getTitle();
        mDataBaseRef.ShowLog("name is "+ name);
        String price = skuDetailsList.get(position).getPrice();
        String description = skuDetailsList.get(position).getDescription();

        holder.productsName.setText(name);
        holder.productsCoins.setText(description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.productsCoins.setTextColor(context.getColor(R.color.Ext_primary_text));
        }
        holder.productsCoins.setTextSize(20);
        holder.productPrice.setText("Rs: " + price);
        holder.btnBuy.setOnClickListener(view -> {

            Toast.makeText(context, "Msg is: "+ skuDetailsList.get(position).getTitle(), Toast.LENGTH_SHORT).show();

            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetailsList.get(position)).build();
            int response = billingClient.launchBillingFlow(appCompatActivity, billingFlowParams).getResponseCode();

            switch (response){
                case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
                    mDataBaseRef.ShowLog("BILLING_UNAVAILABLE");
                    break;
                case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                    mDataBaseRef.ShowLog("DEVELOPER_ERROR");
                    break;
                case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
                    mDataBaseRef.ShowLog("FEATURE_NOT_SUPPORTED");
                    break;
                case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                    mDataBaseRef.ShowLog("ITEM_ALREADY_OWNED");
                    break;
                case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
                    mDataBaseRef.ShowLog("SERVICE_DISCONNECTED");
                    break;
                case BillingClient.BillingResponseCode.SERVICE_TIMEOUT:
                    mDataBaseRef.ShowLog("SERVICE_TIMEOUT");
                    break;
                case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
                    mDataBaseRef.ShowLog("ITEM_UNAVAILABLE");
                    break;
                case BillingClient.BillingResponseCode.USER_CANCELED:
                    mDataBaseRef.ShowLog("USER_CANCELED");
                    break;

                case BillingClient.BillingResponseCode.OK:
                    mDataBaseRef.ShowLog("Billing Flow started.");

                    mDataBaseRef.ShowToast("Billing Flow started.");
                    //TODO: Add shared preference to check new purchase flow.
                    break;

            }


        });

        mDataBaseRef.ShowLog("return convertView");


        return convertView;
    }



    private static class ViewHolder {

        protected TextView productsCoins, productPrice, productsName;
        protected ImageView productImage;
        protected Button btnBuy;

    }

}

package com.nyen.User.esalesawab.UI.Fund;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.Utils.SharedPref;
import com.nyen.User.esalesawab.billing.InnAppPurchase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PlayStore extends AppCompatActivity implements InnAppPurchase.InnAppPurchaseHelperListener, View.OnClickListener {

    private final Context context = PlayStore.this;
    private  FireBaseHelper mDataBaseRef;
    InnAppPurchase innAppPurchase;
    HashMap<String, SkuDetails> skuDetailsHashMap = new HashMap<>();
    //For non_consumable tag "nc" is used at start
    final String MINI_PACK = "mini_pack";
    final String SMALL_PACK = "small_pack";
    final String MEDIUM_PACK = "medium_pack";
    final String LARGE_PACK = "large_pack";
    private final List<String> skuList = Arrays.asList(MINI_PACK, SMALL_PACK, MEDIUM_PACK, LARGE_PACK);


    Button btnBuyP1, btnBuyP2, btnBuyP3, btnBuyP4;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_store);
        mDataBaseRef = new FireBaseHelper(context);
        sharedPref = new SharedPref(context);

        btnBuyP1 = findViewById(R.id.btnBuyP1);
        btnBuyP2 = findViewById(R.id.btnBuyP2);
        btnBuyP3 = findViewById(R.id.btnBuyP3);
        btnBuyP4 = findViewById(R.id.btnBuyP4);



        btnBuyP1.setOnClickListener(this);
        btnBuyP2.setOnClickListener(this);
        btnBuyP3.setOnClickListener(this);
        btnBuyP4.setOnClickListener(this);

        innAppPurchase = new InnAppPurchase(this, this, skuList);

        //Get previous coins from shared pref data
        /*totalCoins = sharedPref.getInt(SETTINGS_COINS);*/
        /*tvTotalCoinsNum.setText(String.format(Locale.getDefault(), "%d", totalCoins));*/
    }




    @Override
    public void onSkuListResponse(HashMap<String, SkuDetails> skuDetails) {
        skuDetailsHashMap = skuDetails;
        mDataBaseRef.ShowLog(   "skuDetailsHashMap = skuDetails assigned ");
        innAppPurchase.getPurchasedItems();
    }
    @Override
    public void onPurchaseHistoryResponse(List<Purchase> purchasedItems) {
        mDataBaseRef.ShowLog(   "onPurchaseHistoryResponse Listener received Data");
        if (purchasedItems != null) {
            mDataBaseRef.ShowLog(   "purchasedItem is not null");
            int i = 0;
            for (Purchase purchase : purchasedItems) {
                i+=i;
                //Update UI and backend according to purchased items if required
                // Like in this project I am updating UI for purchased items
                mDataBaseRef.ShowLog(   "Updating UI with purchase item "+purchase.getSkus().get(i));
                String sku = purchase.getSkus().get(0);
                switch (sku) {
                    case MINI_PACK:
                        innAppPurchase.consumePurchase(purchase);
                        mDataBaseRef.ShowLog(   "Updated UI with purchase item "+ MINI_PACK);
                        break;
                    case SMALL_PACK:
                        innAppPurchase.consumePurchase(purchase);
                        mDataBaseRef.ShowLog(   "Updated UI with purchase item "+ SMALL_PACK);
                        break;
                    case MEDIUM_PACK:
                        innAppPurchase.consumePurchase(purchase);
                        mDataBaseRef.ShowLog(   "Updated UI with purchase item "+MEDIUM_PACK);
                        break;
                    case LARGE_PACK:
                        innAppPurchase.consumePurchase(purchase);
                        mDataBaseRef.ShowLog(   "Updated UI with purchase item "+ LARGE_PACK);
                        break;

                }
            }
        }
    }
    @Override
    public void onPurchaseCompleted(Purchase purchase) {
        mDataBaseRef.ShowLog("Purchase Successful");
        updatePurchase(purchase);
    }
    private void updatePurchase(Purchase purchase){
        String sku = purchase.getSkus().get(0);
        switch (sku) {
            case MINI_PACK:
                mDataBaseRef.ShowLog("Purchased "+ MINI_PACK);
                String orderId = purchase.getOrderId();
                mDataBaseRef.xyz("Purchase 2150 coins from Google Play Store", "Purchase 2150 coins. Order ID: "+orderId,2150, true, null, "2150 coins added to your account.");
                /*tvTotalCoinsNum.setText(String.format(Locale.getDefault(), "%d", totalCoins));*/
                break;

            case SMALL_PACK:
                orderId = purchase.getOrderId();
                mDataBaseRef.ShowLog("Purchased "+ SMALL_PACK);
                mDataBaseRef.xyz("Purchase coins from Google Play Store", "Purchase coins. Order ID: "+orderId,8500, true, null, "8500 coins added to your account.");
                break;

            case MEDIUM_PACK:
                mDataBaseRef.ShowLog("Purchased "+ MEDIUM_PACK);
                orderId = purchase.getOrderId();
                mDataBaseRef.xyz("Purchase coins from Google Play Store", "Purchase coins. Order ID: "+orderId,21250, true, null, "21250 coins added to your account.");

                break;

            case LARGE_PACK:
                mDataBaseRef.ShowLog("Purchased "+ LARGE_PACK);
                orderId = purchase.getOrderId();
                mDataBaseRef.xyz("Purchase coins from Google Play Store", "Purchase coins. Order ID: "+orderId,45000, true, null, "45000 coins added to your account.");

                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (innAppPurchase != null)
            innAppPurchase.endConnection();
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBuyP1:
                mDataBaseRef.ShowLog(   "Click on 25 "+MINI_PACK+" to Buy it. ");
                mDataBaseRef.ShowToast(   "Click on 25 "+MINI_PACK+" to Buy it. ");
                launch(MINI_PACK);

                break;
            case R.id.btnBuyP2:
                launch(SMALL_PACK);
                mDataBaseRef.ShowToast(   "Click on 850 "+SMALL_PACK+" to Buy it. ");
                break;
            case R.id.btnBuyP3:
                launch(MEDIUM_PACK);
                mDataBaseRef.ShowToast(   "Click on 8500 "+MEDIUM_PACK+" to Buy it. ");
                break;
            case R.id.btnBuyP4:
                launch(LARGE_PACK);
                mDataBaseRef.ShowToast(   "Click on 85000 "+LARGE_PACK+" to Buy it. ");
                break;


        }

    }



    private void launch(String sku){
        if(!skuDetailsHashMap.isEmpty()){
            mDataBaseRef.ShowLog(   "Buying "+skuDetailsHashMap.get(sku));
            innAppPurchase.launchBillingFLow(skuDetailsHashMap.get(sku));

        }

    }
}

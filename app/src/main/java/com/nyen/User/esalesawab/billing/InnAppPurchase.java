package com.nyen.User.esalesawab.billing;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.nyen.User.esalesawab.Database.FireBaseHelper;

import java.util.HashMap;
import java.util.List;

public class InnAppPurchase {
    private final FireBaseHelper mDataBaseRef;

    private final Context context;
    private BillingClient mBillingClient;
    private InnAppPurchaseHelperListener innAppPurchaseHelperListener;
    private List<String> skuList;

    private static final long RECONNECT_TIMER_START_MILLISECONDS = 1L * 1000L;
    private static final long RECONNECT_TIMER_MAX_TIME_MILLISECONDS = 1000L * 60L * 15L; // 15 mins
    private long reconnectMilliseconds = RECONNECT_TIMER_START_MILLISECONDS;
    private static final Handler handler = new Handler(Looper.getMainLooper());

    /**
     * To instantiate the object
     *  @param context           It will be used to get an application context to bind to the in-app billing service.
     * @param innAppPurchaseHelperListener Your listener to get the response for your query.
     * @param skuList it uses list of product.
     */
    public InnAppPurchase(Context context, InnAppPurchaseHelperListener innAppPurchaseHelperListener, List<String> skuList) {
        this.context = context;
        this.innAppPurchaseHelperListener = innAppPurchaseHelperListener;
        this.skuList = skuList;
        this.mBillingClient = BillingClient.newBuilder(context)
                .enablePendingPurchases()
                .setListener(getPurchaseUpdatedListener())
                .build();
        mDataBaseRef = new FireBaseHelper(context);
        if (!mBillingClient.isReady()) {
            mDataBaseRef.ShowLog("BillingClient: Start connection...");
            startConnection();
        }
    }

    /**
     * To establish the connection with play library
     * It will be used to notify that setup is complete and the billing
     * client is ready. You can query whatever you want.
     */
    private void startConnection() {
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                int billingResponseCode = billingResult.getResponseCode();
                mDataBaseRef.ShowLog("onBillingSetupFinished: " + billingResult.getResponseCode());
                if (billingResponseCode == BillingClient.BillingResponseCode.OK) {
                    reconnectMilliseconds = RECONNECT_TIMER_START_MILLISECONDS;
                    getSKUDetails(skuList);
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                mDataBaseRef.ShowLog(   "onBillingServiceDisconnected: ");
                retryBillingServiceConnectionWithExponentialBackoff();
            }
        });
    }

    private void retryBillingServiceConnectionWithExponentialBackoff() {
        handler.postDelayed(this::startConnection,
                reconnectMilliseconds);
        reconnectMilliseconds = Math.min(reconnectMilliseconds * 2,
                RECONNECT_TIMER_MAX_TIME_MILLISECONDS);
    }

    /**
     * Get purchases details for all the items bought within your app.
     */
    public void getPurchasedItems() {
        mDataBaseRef.ShowLog(   "getting purchased item ");
        mBillingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP, new PurchasesResponseListener() {
            @Override
            public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
                mDataBaseRef.ShowLog(   "purchased item query Received ");
                if (innAppPurchaseHelperListener != null){
                    mDataBaseRef.ShowLog(   "setting onPurchaseHistoryResponse Listener");
                    innAppPurchaseHelperListener.onPurchaseHistoryResponse(list);
                }

            }
        });
    }

    /**
     * Perform a network query to get SKU details and return the result asynchronously.
     */
    public void getSKUDetails(List<String> skuList) {
        mDataBaseRef.ShowLog(   "getting SKUDetails ");
        final HashMap<String, SkuDetails> skuDetailsHashMap = new HashMap<>();
        SkuDetailsParams skuParams = SkuDetailsParams.newBuilder().setType(BillingClient.SkuType.INAPP).setSkusList(skuList).build();
        mBillingClient.querySkuDetailsAsync(skuParams, new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                    mDataBaseRef.ShowLog(   "onSkuDetailsResponse Received ");
                    for (SkuDetails skuDetails : skuDetailsList) {
                        skuDetailsHashMap.put(skuDetails.getSku(), skuDetails);
                    }
                    if (innAppPurchaseHelperListener != null) {
                        mDataBaseRef.ShowLog("onSkuListResponse Listener set ");
                        innAppPurchaseHelperListener.onSkuListResponse(skuDetailsHashMap);
                    }
                }
            }
        });
    }

    /**
     * Initiate the billing flow for an in-app purchase or subscription.
     *
     * @param skuDetails skudetails of the product to be purchased
     *                   Developer console.
     */
    public void launchBillingFLow(final SkuDetails skuDetails) {
        mDataBaseRef.ShowLog(   "Billing flow starting ");
        if(mBillingClient.isReady()){
            BillingFlowParams mBillingFlowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetails)
                    .build();

            mBillingClient.launchBillingFlow((Activity) context, mBillingFlowParams);
            mDataBaseRef.ShowLog(   "Billing flow started ");
        }
    }

    /**
     * Your listener to get the response for purchase updates which happen when, the user buys
     * something within the app or by initiating a purchase from Google Play Store.
     */
    private PurchasesUpdatedListener getPurchaseUpdatedListener() {
        return (billingResult, purchases) -> {

            mDataBaseRef.ShowLog(   "getPurchaseUpdatedListener Started ");
            int responseCode = billingResult.getResponseCode();
            if (responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                mDataBaseRef.ShowLog(   "response is ok and purchases is not null ");
                //here when purchase completed
                for (Purchase purchase : purchases) {
                    //I have named sku in such a way that I get sku name as "type_name" for ex: "nc_ring"
                    //For non consumable I will acknowledge purchase
                    //For consumable I will consume purchase
                    String type = purchase.getSkus().get(0).split("_")[0];
                    if(type.equals("nc")){
                        mDataBaseRef.ShowLog(   "Purchase from Acknowledge");

                        mDataBaseRef.ShowLog(   "Purchase is "+ purchase.getSkus().get(0));
                        acknowledgePurchase(purchase);
                    }

                    else {
                        mDataBaseRef.ShowLog(   "Purchase from Consume");
                        mDataBaseRef.ShowLog(   "Purchase is "+ purchase.getSkus().get(0));
                        consumePurchase(purchase);
                    }
                }
            }else if (responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED && purchases !=null) {
                mDataBaseRef.ShowLog(   "Item Already owned");


            }
            else if (responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
                mDataBaseRef.ShowLog(   "user cancelled");
            } else if (responseCode == BillingClient.BillingResponseCode.SERVICE_DISCONNECTED) {
                mDataBaseRef.ShowLog( "service disconnected");
                startConnection();
            }
        };
    }

    public void acknowledgePurchase(Purchase purchase) {

        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED
                && isSignatureValid(purchase)) {
            mDataBaseRef.ShowLog(   "Purchase State is Purchased and Signature is valid");

            //This is for Non_Consumable product
            AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.getPurchaseToken())
                    .build();
            mBillingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
                @Override
                public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                    mDataBaseRef.ShowLog("purchase: "+ "Purchase Acknowledged");
                }
            });

            if (innAppPurchaseHelperListener != null) {
                innAppPurchaseHelperListener.onPurchaseCompleted(purchase);
                mDataBaseRef.ShowLog(   "onPurchaseCompleted Listener set");
            }
        }
    }

    public void consumePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED
                && isSignatureValid(purchase)) {
            mDataBaseRef.ShowLog(   "Purchase State is Purchased and Signature is valid");

            //This is for Consumable product
            ConsumeParams consumeParams = ConsumeParams.newBuilder()
                    .setPurchaseToken(purchase.getPurchaseToken())
                    .build();
            mBillingClient.consumeAsync(consumeParams, new ConsumeResponseListener() {
                @Override
                public void onConsumeResponse(@NonNull BillingResult billingResult, @NonNull String s) {
                    mDataBaseRef.ShowLog("purchase: Purchase Consumed");
                    if (innAppPurchaseHelperListener != null)
                        innAppPurchaseHelperListener.onPurchaseCompleted(purchase);
                    mDataBaseRef.ShowLog(   "onPurchaseCompleted Listener set for Consume");
                }
            });


        }
    }



    private boolean isSignatureValid(Purchase purchase) {
        return Security.verifyPurchase(purchase.getOriginalJson(), purchase.getSignature());
    }

    /**
     * Call this method once you are done with this BillingClient reference.
     */
    public void endConnection() {
        if (mBillingClient != null && mBillingClient.isReady()) {
            mBillingClient.endConnection();
            mBillingClient = null;
        }
    }

    /**
     * Listener interface for handling the various responses of the Purchase helper util
     */
    public interface InnAppPurchaseHelperListener {
        void onSkuListResponse(HashMap<String, SkuDetails> skuDetailsHashMap);
        void onPurchaseHistoryResponse(List<Purchase> purchasedItem);
        void onPurchaseCompleted(Purchase purchase);
    }

}

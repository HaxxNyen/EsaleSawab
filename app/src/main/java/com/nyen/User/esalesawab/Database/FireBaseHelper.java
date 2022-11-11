package com.nyen.User.esalesawab.Database;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nyen.User.esalesawab.Listener.AddBonusListener;
import com.nyen.User.esalesawab.Model.FundTransfer;
import com.nyen.User.esalesawab.Model.User;
import com.nyen.User.esalesawab.Utils.Cons;
import com.nyen.User.esalesawab.Utils.CustomProgressDialog;
import com.nyen.User.esalesawab.Utils.SharedPref;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FireBaseHelper {


    // parameters
    private final Context context;
    private final DatabaseReference databaseReference;
    private static boolean isPersistenceEnabled = false;
    private CustomProgressDialog Dialog;
    private String Key;
    private final SharedPref sharedPref;

    private int coinsOfAdmin;
    private int coinsOfUser;


    public FireBaseHelper(Context context) {
        this.context = context;    // context can be used to call PreferenceManager etc.
        if (!isPersistenceEnabled) {

            FirebaseDatabase.getInstance().setPersistenceEnabled(false);
            isPersistenceEnabled = true;

        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        sharedPref = new SharedPref(context);
    }


    //Functions.......................................................
    public void shareAPP(String code) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, "Esal-e-Sawab");
        share.putExtra(Intent.EXTRA_TEXT, "HI," + sharedPref.getStr(Cons.USER_PHONE) + "\n has invited you to join Esal-e-Sawab.\n " +
                "Esal-e-Sawab is a small Platform in which you can request for Esal-e-Sawab for your marhoomen and recite for other marhoomen." +
                " \n Join Now and get " + Cons.SIGNUP_COINS_BONUS +
                " Coins Sign Up bonus and also using this code " +
                code + " can get extra " + Cons.REFERALL_BONUS + " Coins" +
                "  \n https://play.google.com/store/apps/details?id=com.nyen.User.esalesawab");
        context.startActivity(Intent.createChooser(share, "Refer Using"));

    }

    public void ShowToast(String Msg) {
        Toast.makeText(context, Msg, Toast.LENGTH_SHORT).show();
    }

    public void ShowLog(String Msg) {
        String TAG = "Pagal";
        Log.d(TAG, Msg);
    }

    public void showLoading() {
        Dialog = new CustomProgressDialog(context);
        Dialog.show();
    }

    public void hideLoading() {
        Dialog.dismiss();
    }

    public void DeleteMarhoom(final Context context, final String id) {
        getMarhomeenRef().child(id).removeValue().addOnCompleteListener(task ->
                Toast.makeText(context, "deleted Successfully ", Toast.LENGTH_SHORT).show());

    }

    public void DeleteArticle(final Context context, final String id) {
        getArticlesRef().child(id).removeValue().addOnCompleteListener(task ->
                Toast.makeText(context, "deleted Successfully ", Toast.LENGTH_SHORT).show());


    }

    public String titleWithoutAppName(String text) {
        String skuTitleAppNameRegex = "(?> \\(.+?\\))$";
        Pattern p = Pattern.compile(skuTitleAppNameRegex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        return m.replaceAll("");
    }

    //Start XYZ func()
    public void xyz(String DescriptionDr, String DescriptionCr, int requestCoins, boolean Add, Map<String, Object> childUpdateReceived, String msg) {

        Map<String, Object> childUpdate = new HashMap<>();
        if (childUpdateReceived != null){
            childUpdate.putAll(childUpdateReceived);
        }

        if (Add) {
            // 1- Add Coins to User


            //Debit Transaction
            Key = getFundTransferRef().push().getKey();
            FundTransfer fundTransferDR = new FundTransfer(Key, DescriptionDr, sharedPref.getStr(Cons.ADMIN_ID),
                    sharedPref.getStr(Cons.USER_ID), String.valueOf(requestCoins), "Dr", "Pending", System.currentTimeMillis());
            childUpdate.put("/FundTransfer/" + Key, fundTransferDR);

            //Credit Transaction
            Key = getFundTransferRef().push().getKey();
            FundTransfer fundTransferCR = new FundTransfer(Key, DescriptionCr, sharedPref.getStr(Cons.USER_ID),
                    sharedPref.getStr(Cons.ADMIN_ID), String.valueOf(requestCoins), "Cr", "Pending", System.currentTimeMillis());
            childUpdate.put("/FundTransfer/" + Key, fundTransferCR);

            //Add Coins to User account.

            ShowLog("xyz-before coins add"+sharedPref.getInt(Cons.COINS));
            coinsOfUser = sharedPref.getInt(Cons.COINS) + requestCoins;
            ShowLog("xyz-before++ coins add"+sharedPref.getInt(Cons.COINS));

            childUpdate.put("/Users/" + sharedPref.getStr(Cons.USER_ID) + "/coins", coinsOfUser);


            //Subtract Coins from Admin account.
            ShowLog("xyz-before coins add"+sharedPref.getInt(Cons.ADMIN_COINS));
            coinsOfAdmin = sharedPref.getInt(Cons.ADMIN_COINS) - requestCoins;
            ShowLog("xyz-before++ coins add"+sharedPref.getInt(Cons.ADMIN_COINS));
            childUpdate.put("/Users/" + sharedPref.getStr(Cons.ADMIN_ID) + "/coins", coinsOfAdmin);


        } else {
            // 2- Add Coins to Admin



            //Debit Transaction
            Key = getFundTransferRef().push().getKey();
            FundTransfer fundTransferDR = new FundTransfer(Key, DescriptionDr, sharedPref.getStr(Cons.USER_ID),
                    sharedPref.getStr(Cons.ADMIN_ID), String.valueOf(requestCoins), "Dr", "Pending", System.currentTimeMillis());
            childUpdate.put("/FundTransfer/" + Key, fundTransferDR);


            //Credit Transaction
            Key = getFundTransferRef().push().getKey();
            FundTransfer fundTransferCR = new FundTransfer(Key, DescriptionCr, sharedPref.getStr(Cons.ADMIN_ID),
                    sharedPref.getStr(Cons.USER_ID), String.valueOf(requestCoins), "Cr", "Pending", System.currentTimeMillis());
            childUpdate.put("/FundTransfer/" + Key, fundTransferCR);

            //Add Coins to User account.
            coinsOfUser = sharedPref.getInt(Cons.COINS) - requestCoins;

            childUpdate.put("/Users/" + sharedPref.getStr(Cons.USER_ID) + "/coins", coinsOfUser);

            //Subtract Coins from Admin account.
            coinsOfAdmin = sharedPref.getInt(Cons.ADMIN_COINS) + requestCoins;
            childUpdate.put("/Users/" + sharedPref.getStr(Cons.ADMIN_ID) + "/coins", coinsOfAdmin);

        }

        //Sent query to Realtime DataBase.
        databaseReference.updateChildren(childUpdate).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                sharedPref.setInt(Cons.COINS, coinsOfUser);
                sharedPref.setInt(Cons.ADMIN_COINS, coinsOfAdmin);
                ShowLog("xyz-after coins add"+sharedPref.getInt(Cons.COINS));
                ShowLog("xyz-after coins add"+sharedPref.getInt(Cons.ADMIN_COINS));
                ShowToast(msg);
                ShowLog(msg);
            } else {
                ShowToast("Something wrong");
                ShowLog("Something wrong");
            }
        });



    }


    //Ended XYZ func()

    public void setChildrenQuery(Map<String, Object> childUpdate1, String msg){
        databaseReference.updateChildren(childUpdate1).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ShowLog("setChildrenQuery-after++ coins add"+sharedPref.getInt(Cons.COINS));
                ShowLog("setChildrenQuery-after++ coins add"+sharedPref.getInt(Cons.ADMIN_COINS));
                ShowToast(msg);
                ShowLog(msg);
            } else {
                ShowToast("Something wrong");
                ShowLog("Something wrong");
            }
        });
    }

    public void mAddReferralBonus(String DescriptionDr, String DescriptionCr, String id, int requestCoins, AddBonusListener addBonusListener) {

        ShowLog("Referal bonus started");

        getUsersRef().orderByChild("uid").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        User user = ds.getValue(User.class);

                        assert user != null;
                        coinsOfUser = requestCoins + user.getCoins();
                        ShowLog("Coins of user "+coinsOfUser);
                    }


                    // 1- Add Coins to User Referrer
                    Map<String, Object> childUpdate = new HashMap<>();

                    //Debit Transaction
                    Key = getFundTransferRef().push().getKey();
                    FundTransfer fundTransferDR = new FundTransfer(Key, DescriptionDr, sharedPref.getStr(Cons.ADMIN_ID),
                            id, String.valueOf(requestCoins), "Dr", "Pending", System.currentTimeMillis());
                    childUpdate.put("/FundTransfer/" + Key, fundTransferDR);

                    //Credit Transaction
                    Key = getFundTransferRef().push().getKey();
                    FundTransfer fundTransferCR = new FundTransfer(Key, DescriptionCr, id,
                            sharedPref.getStr(Cons.ADMIN_ID), String.valueOf(requestCoins), "Cr", "Pending", System.currentTimeMillis());
                    childUpdate.put("/FundTransfer/" + Key, fundTransferCR);

                    //Add Coins to User Referrer account.
                    childUpdate.put("/Users/"+id+"/coins", coinsOfUser);


                    //Subtract Coins from Admin account.
                    coinsOfAdmin = sharedPref.getInt(Cons.ADMIN_COINS) - requestCoins;
                    childUpdate.put("/Users/" + sharedPref.getStr(Cons.ADMIN_ID) + "/coins", coinsOfAdmin);

                    ShowLog("Coins of user after updated in childupdate "+ childUpdate.get("/Users/" + sharedPref.getStr(Cons.ADMIN_ID) + "/coins"));

                    //Update status that User redeemed bonus.
                    childUpdate.put("/BonusRedemption/" + sharedPref.getStr(Cons.USER_CODE_KEY) + "/status", true);

                    //Update referrer Id.
                    childUpdate.put("/BonusRedemption/" + sharedPref.getStr(Cons.USER_CODE_KEY) + "/refererCode", id);

                    //Sent query to Realtime DataBase.
                    databaseReference.updateChildren(childUpdate).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            sharedPref.setInt(Cons.ADMIN_COINS, coinsOfAdmin);
                            ShowToast("Hurry! Referrer Awarded with Bonus");
                            ShowLog("Hurry! Referrer Awarded with Bonus");
                            addBonusListener.onBonusAdded();
                        } else {
                            ShowToast("Something wrong");
                            ShowLog("Something wrong");
                        }
                    });

                } else {
                    Toast.makeText(context, "Sorry User id not exist", Toast.LENGTH_SHORT).show();
                    ShowLog("Sorry User id not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "On Error: " + error, Toast.LENGTH_SHORT).show();
                ShowLog("On Error: " + error);

            }
        });




    }







    //References.......................................................
    public DatabaseReference getUsersRef() {
        return databaseReference.child("Users");
    }

    public DatabaseReference getReferralRef() {
        return databaseReference.child("Referral");
    }

    public DatabaseReference getFundTransferRef() {
        return databaseReference.child("FundTransfer");
    }

    public DatabaseReference getMarhomeenRef() {
        return databaseReference.child("Marhomeen");
    }

    public DatabaseReference getEsaleSawabRequestRef() {
        return databaseReference.child("EsaleSawabRequest");
    }

    public DatabaseReference getAcceptedESRRef() {
        return databaseReference.child("AcceptedESR");
    }

    public DatabaseReference getArticlesRef() {
        return databaseReference.child("Articles");
    }

    public DatabaseReference getWithdrawRef() {
        return databaseReference.child("Withdraw");
    }

    public DatabaseReference getWithdrawValueRef() {
        return databaseReference.child("WithdrawValue");
    }

    public DatabaseReference getAdSettingsRef() {
        return databaseReference.child("AdSettings");
    }

    public DatabaseReference getAppSettingsRef() {
        return databaseReference.child("AppSettings");
    }

    public DatabaseReference getLocalProductRef() {
        return databaseReference.child("LocalProduct");
    }

    public DatabaseReference getLocalProductTrxRef() {
        return databaseReference.child("LocalProductTrx");
    }

    public DatabaseReference getDailyRewardRef() {
        return databaseReference.child("DailyReward");
    }

    public DatabaseReference getBonusRedemptionRef() {
        return databaseReference.child("BonusRedemption");
    }


}


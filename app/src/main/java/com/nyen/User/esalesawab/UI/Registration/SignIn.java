package com.nyen.User.esalesawab.UI.Registration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.MainActivity;
import com.nyen.User.esalesawab.Model.BonusRedemption;
import com.nyen.User.esalesawab.Model.User;
import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.Utils.Cons;
import com.nyen.User.esalesawab.Utils.SharedPref;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * A login screen that offers login via Phone Number.
 */
public class SignIn extends AppCompatActivity implements OnClickListener {

    EditText mMobileNumber, mOTP;
    Boolean mVerificationInProgress = false;
    Button mBtnSignIn;
    ProgressBar mProgressBar;
    Context context = SignIn.this;
    private String mVerificationId;
    private final String TAG = "Pagal";
    LinearLayout mSignIn_step2;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;

    private FireBaseHelper mDataBaseRef;
    private FirebaseUser userdata;
    private String mobile;
    private User userAdmin;
    private boolean mCreateUser = false;
    private SharedPref sharedPref;
    private CountryCodePicker ccp;
    private String n;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        sharedPref = new SharedPref(context);
        mDataBaseRef = new FireBaseHelper(context);

        mMobileNumber = findViewById(R.id.et_signin_number);
        mOTP = findViewById(R.id.et_signin_otp);
        mBtnSignIn = findViewById(R.id.btn_signin);
        mProgressBar = findViewById(R.id.login_progress);
        mSignIn_step2 = findViewById(R.id.signIn_step2);
        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(mMobileNumber);

        mBtnSignIn.setOnClickListener(this);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mDataBaseRef.hideLoading();
                mBtnSignIn.setText("Verify Code");
                mOTP.requestFocus();
                mBtnSignIn.setEnabled(true);

                mVerificationId = s;

            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                String code = phoneAuthCredential.getSmsCode();
                if (code != null) {
                    Log.d(TAG, "smsCode:" + code);
                    mOTP.setText(code);

                }

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(context, "is this Error:" + e, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Msg is: " + e);
                mBtnSignIn.setEnabled(true);
                mDataBaseRef.hideLoading();

            }
        };

    }

    private void getAdminBalance() {

        mDataBaseRef.getUsersRef().orderByChild(Cons.USER_PHONE).equalTo("03027860317").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        userAdmin = ds.getValue(User.class);
                    }
                    mCreateUser = true;
                } else {
                    mCreateUser = false;
                }
                createAccount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "On Error: " + error, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void createAccount() {


        mDataBaseRef.getUsersRef().child(userdata.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mDataBaseRef.hideLoading();
                    gotoMainActivity();
                } else {

                    //TODO Replace each phone format where it is used.
                    mobile = ccp.getFullNumberWithPlus();
                    mDataBaseRef.hideLoading();
                    User user = new User(userdata.getUid(), "Guest", mobile, "", "", Cons.SIGNUP_COINS_BONUS, Cons.SIGNUP_XP_BONUS, System.currentTimeMillis());
                    mDataBaseRef.getUsersRef().child(userdata.getUid()).setValue(user).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            if (mCreateUser) {
                                sharedPref.setInt(Cons.ADMIN_COINS, userAdmin.getCoins());
                                sharedPref.setStr(Cons.ADMIN_ID, userAdmin.getUid());
                                sharedPref.setStr(Cons.USER_ID, userdata.getUid());
                                sharedPref.setInt(Cons.COINS, 0);

                                mDataBaseRef.ShowLog("User Id: "+sharedPref.getStr(Cons.USER_ID));
                                /*mDataBaseRef.ShowLog("User Coins: "+ sharedPref.getInt(Cons.COINS));*/
                                mDataBaseRef.ShowLog("Admin Id: "+sharedPref.getStr(Cons.ADMIN_ID));
                                /*mDataBaseRef.ShowLog("Admin coins: "+ sharedPref.getInt(Cons.ADMIN_COINS));*/

                                mDataBaseRef.xyz("Signup Bonus", "Signup Bonus", Cons.SIGNUP_COINS_BONUS, true, null, "Signup bonus added to your account.");
                                initiateInviteCode();

                            }
                            Toast.makeText(context, "User Created.", Toast.LENGTH_SHORT).show();
                            gotoMainActivity();


                        } else {
                            Toast.makeText(context, "Error: " + task1.getException(), Toast.LENGTH_LONG).show();
                        }

                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error: " + error, Toast.LENGTH_LONG).show();


            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_signin) {
            if (!mVerificationInProgress) {
                if (!ccp.isValidFullNumber()) {
                    mMobileNumber.requestFocus();
                    mMobileNumber.setError("Please enter valid Mobile Number");
                    return;
                }
                sendVerificationCode();
                mVerificationInProgress = true;
            } else {
                signIn();

            }
        }

    }

    private void signIn() {
        mDataBaseRef.showLoading();
        String code = mOTP.getText().toString();
        verifyCode(code);
        mBtnSignIn.setEnabled(false);


    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userdata = Objects.requireNonNull(task.getResult()).getUser();

                getAdminBalance();


            } else {
                String e = Objects.requireNonNull(task.getException()).getMessage();
                Toast.makeText(context, "Error: " + e, Toast.LENGTH_LONG).show();
                mBtnSignIn.setEnabled(true);
                mDataBaseRef.hideLoading();

            }
        });
    }

    private void initiateInviteCode() {
        Random rnd = new Random();
        n = String.valueOf(100000000 + rnd.nextInt(900000000));

        mDataBaseRef.getBonusRedemptionRef().orderByChild(Cons.CODE).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        BonusRedemption referral = ds.getValue(BonusRedemption.class);
                        assert referral != null;
                        if (referral.getCode().equals(n)) {
                            Random rnd = new Random();
                            n = String.valueOf(100000000 + rnd.nextInt(900000000));
                        }
                    }
                }

                String Key = mDataBaseRef.getBonusRedemptionRef().push().getKey();
                BonusRedemption bonusRedemption = new BonusRedemption(Key, userdata.getUid(), n, "", false, System.currentTimeMillis());
                assert Key != null;
                mDataBaseRef.getBonusRedemptionRef().child(Key).setValue(bonusRedemption).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Code generated", Toast.LENGTH_SHORT).show();
                        sharedPref.setStr(Cons.USER_CODE, n);
                        sharedPref.setStr(Cons.USER_CODE_KEY, Key);

                    }

                });

                mDataBaseRef.hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error: " + error, Toast.LENGTH_SHORT).show();
                mDataBaseRef.ShowLog("Error On Getting UserCode Generation : " + error);
                mDataBaseRef.hideLoading();

            }
        });
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void sendVerificationCode() {

        mDataBaseRef.showLoading();
        mSignIn_step2.setVisibility(View.VISIBLE);
        mBtnSignIn.setText("Wait");
        mBtnSignIn.setEnabled(false);


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                ccp.getFullNumberWithPlus(),
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }


}
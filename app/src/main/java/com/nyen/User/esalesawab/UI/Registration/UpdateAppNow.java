package com.nyen.User.esalesawab.UI.Registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nyen.User.esalesawab.R;
import com.nyen.User.esalesawab.Utils.AppUpdate;

public class UpdateAppNow extends AppCompatActivity {
    private final Context context = UpdateAppNow.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_app_now);
        new AppUpdate(context).setFirebaseVersion();

        Button btnUpdate = findViewById(R.id.btnUpdateAppNow);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName())));
            }
        });
    }
}
package com.nyen.User.esalesawab.Help;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.nyen.User.esalesawab.R;

public class NeedHelp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_help);
        TextView btnWhatsapp = findViewById(R.id.btnWhatsapp);
        TextView btnGmail = findViewById(R.id.btnGmail);


        btnWhatsapp.setOnClickListener(view -> {

            String url = "https://wa.me/923027860317?text=Help%20about%20fund%20deposit";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);


        });

        btnGmail.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:haxxnyen@gmail.com")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_SUBJECT, "Help About Fund Deposit");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }

        });

    }
}

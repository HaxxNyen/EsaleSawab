package com.nyen.User.esalesawab.FCM;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nyen.User.esalesawab.R;

import java.util.Objects;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        getMessage(Objects.requireNonNull(remoteMessage.getNotification()).getTitle(), remoteMessage.getNotification().getBody());
    }

    private void getMessage(String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "mEsaleSawabChannel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(101, builder.build());
    }
}

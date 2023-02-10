package com.techno71.fireservice.Model;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.techno71.fireservice.R;
import com.techno71.fireservice.View.FireMapActivity;
import com.techno71.fireservice.View.UserMapActivity;
import com.techno71.fireservice.forgaund.Sound;
import com.techno71.fireservice.forgaund.Window;

import java.util.Map;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MessagingService extends FirebaseMessagingService {

    private final String channelId = "100";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

       /* sharedPreferences_type = getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

        user_type = sharedPreferences_type.getString("user_type", "not found");


        Map<String, String> extraData = remoteMessage.getData();
        sharedPreferences_type.edit().putFloat("latitude001", Float.parseFloat("" + extraData.get("latitude"))).apply();
        sharedPreferences_type.edit().putFloat("longitude001", Float.parseFloat(extraData.get("longitude"))).apply();
        sharedPreferences_type.edit().putString("img", "" + extraData.get("img")).apply();
        sharedPreferences_type.edit().putString("imgType", "" + extraData.get("imgType")).apply();

        //Toast.makeText(this, ""+extraData.get("img"), Toast.LENGTH_SHORT).show();

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        // playing audio and vibration when user se reques
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), soundUri);
        Sound.playSound(getApplicationContext());
        // r.play();
        //  r.setLooping(true,);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.setLooping(false);
        }

        // vibration
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 300, 300, 300};
        v.vibrate(pattern, -1);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID");
        //            builder.setSmallIcon(R.drawable.icontrans);
        builder.setSmallIcon(R.drawable.ic_baseline_notifications_24);

        if (user_type.contains("1")) {

            //r.stop();
            resultIntent = new Intent(this, FireMapActivity.class);

        } else {
            //r.stop();
            resultIntent = new Intent(this, UserMapActivity.class);
        }

        // Intent resultIntent = new Intent(this, UserMapActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_MUTABLE);
        builder.setContentTitle(remoteMessage.getNotification().getTitle());
        builder.setContentText(remoteMessage.getNotification().getBody());
        builder.setContentIntent(pendingIntent);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()));
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        //builder.setWhen(System.currentTimeMillis());

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();

        //  builder.setTimeoutAfter(startTime);

        mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);

            channel.setSound(soundUri, audioAttributes);
            channel.enableLights(true);
            channel.enableVibration(true);
            mNotificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

// notificationId is a unique int for each notification that you must define
        mNotificationManager.notify(100, builder.build());*/
        notifyUser(remoteMessage);

    }

    private void notifyUser(RemoteMessage remoteMessage) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);
        String userType = sharedPreferences.getString("user_type", "not found");

        Map<String, String> extraData = remoteMessage.getData();
        sharedPreferences.edit().putFloat("latitude001", Float.parseFloat("" + extraData.get("latitude"))).apply();
        sharedPreferences.edit().putFloat("longitude001", Float.parseFloat("" + extraData.get("longitude"))).apply();
        sharedPreferences.edit().putString("img", "" + extraData.get("img")).apply();
        sharedPreferences.edit().putString("imgType", "" + extraData.get("imgType")).apply();


        Intent intent;
        if (userType.contains("1")) {
            intent = new Intent(this, FireMapActivity.class);
        } else {
            intent = new Intent(this, UserMapActivity.class);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "FireService Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "Your_channel_id")
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setAutoCancel(true)
                .setContentTitle(extraData.get("title"))
                .setContentText(extraData.get("body"))
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX);

        // vibration
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 100, 100};
        v.vibrate(pattern, -1);
        //sound
        Sound.playSound(this);

        notificationManager.notify(Integer.parseInt(channelId), notificationBuilder.build());

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }
}

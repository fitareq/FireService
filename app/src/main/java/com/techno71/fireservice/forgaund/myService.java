package com.techno71.fireservice.forgaund;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;


import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techno71.fireservice.MainActivity;
import com.techno71.fireservice.R;

public class myService extends Service {
    private static final String CHANNEL_ID = "NotificationChannelID";
    public myService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // create the custom or default notification
        // based on the android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        // create an instance of Window class
        // and display the content on screen
        Window window=new Window(this);
        window.open();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    // for android version >=O we need to create
    // custom notification stating
    // foreground service is running
    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_MIN);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Service running")
                .setContentText("Displaying over other apps")

                // this is important, otherwise the notification will show the way
                // you want i.e. it will show some default notification
                .setSmallIcon(R.drawable.ic_launcher_foreground)

                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private PendingIntent pendingIntent;
    private  Notification[] notification;
    private NotificationChannel notificationChannel;
    private NotificationManager notificationManager ;

    public void NotificationUpdate(Integer timeLeft){
        try {
            Intent notificationIntent = new Intent(this, MainActivity.class);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
            }else {
                pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            }

            notification= new Notification[]{new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Bekar Agent App Refreshing")
                    .setContentText("Refresh Time Running : " + timeLeft.toString().replace("-",""))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .build()};
            startForeground(1, notification[0]);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = new NotificationChannel(CHANNEL_ID, "My Counter Service", NotificationManager.IMPORTANCE_DEFAULT);

            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                notificationManager = getSystemService(NotificationManager.class);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        catch (Exception e) {

        }
    }

}

package com.theodoilamviec;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.theodoilamviec.theodoilamviec.R;

public class MyApplication extends Application {

    public static final String CHANNEL_ID = "TheoDoiCongViec";
    @Override
    public void onCreate() {
        super.onCreate();

        SingletonSharePreferences.getInstance().init(this);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Test", importance);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}

package com.theodoilamviec;

import static com.theodoilamviec.MyApplication.CHANNEL_ID;

import android.Manifest;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.FirebaseDatabase;
import com.theodoilamviec.theodoilamviec.DB.APP_DATABASE;
import com.theodoilamviec.theodoilamviec.R;

import java.util.Random;

public class BroadcastReminder extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String notification = intent.getStringExtra("NOTIFICATION");
        String nameProject = intent.getStringExtra("name_project");
        String idProject = intent.getStringExtra("id_project");
        String idJob = intent.getStringExtra("id_job");


        if (notification != null && notification.equals("1")) {
            showNotification(nameProject, idProject, idJob, context);
        }
    }

    void showNotification(String title, String idProject, String idJob, Context context) {

        FirebaseDatabase.getInstance().getReference("Notifications")
                .child(idProject)
                .child(idJob)
                .setValue(title);

        APP_DATABASE.requestDatabase(context).dao().deleteJobNotificationLocalById(idProject, idJob);

        // Build the notification and add the action.
        Notification newMessageNotification = new Notification.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.iconthongbao)
                .setContentTitle(title)
                .setContentText("Đã đến ngày deadline!")
                .build();

        // Issue the notification.
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(new Random().nextInt() + 100, newMessageNotification);

    }
}

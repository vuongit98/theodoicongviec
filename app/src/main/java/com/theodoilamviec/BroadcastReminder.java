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

import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;

public class BroadcastReminder extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String notification = intent.getStringExtra("NOTIFICATION");
        String nameProject = intent.getStringExtra("name_project");
        String idProject = intent.getStringExtra("id_project");
        String idJob = intent.getStringExtra("id_job");
        String nameJob = intent.getStringExtra("name_job");
        String contentJob = intent.getStringExtra("content_job");

        HashMap<String, String > notificationTest = new HashMap<>();
        notificationTest.put("nameJob", nameJob);
        notificationTest.put("contentJob", contentJob);
        if (notification != null && notification.equals("1")) {
            showNotification(notificationTest, idProject, idJob, context);
        }
    }

    void showNotification(HashMap<String, String> data , String idProject, String idJob, Context context) {

        String nameProject = data.get("nameJob");
        String valueJob =data.get("contentJob");
        FirebaseDatabase.getInstance().getReference("Notifications")
                .child(idProject)
                .child(idJob)
                .setValue(data);

        APP_DATABASE.requestDatabase(context).dao().deleteJobNotificationLocalById(idProject, idJob);

        // Build the notification and add the action.
        Notification newMessageNotification = new Notification.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.iconthongbao)
                .setContentTitle(nameProject)
                .setContentText(valueJob + " đã đến ngày deadline!")
                .build();

        // Issue the notification.
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(new Random().nextInt() + 100, newMessageNotification);

    }
}

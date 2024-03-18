package com.theodoilamviec.service;

import static com.theodoilamviec.MyApplication.CHANNEL_ID;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theodoilamviec.Account.Job;
import com.theodoilamviec.Account.JobNotificationLocal;
import com.theodoilamviec.Account.JobUser;
import com.theodoilamviec.Project;
import com.theodoilamviec.theodoilamviec.R;

import java.util.ArrayList;
import java.util.List;

public class BackgroundNotificationService extends Service {

    private final List<JobNotificationLocal> jobsNotificationList = new ArrayList<>();
    private final List<JobUser> usersList = new ArrayList<>();

    private final List<Job> jobsList = new ArrayList<>();
    private final List<Project> projectsList = new ArrayList<>();


    private int count = 0;
    private final String uid = FirebaseAuth.getInstance().getUid();

    boolean flagJob = true ;
    boolean flagJobUser = true ;
    boolean flagJobNotification = true ;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        Bundle bundle = intent.getExtras();
//        if (bundle != null) {
//            List<Project> projectList =bundle.getParcelableArrayList("project");
//            projectsList.clear();
//            assert projectList != null;
//            projectsList.addAll(projectList);
//        }
//        Thread thread = getThread();
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        Thread threadJobUser = getThreadJobUser();
//        try {
//            threadJobUser.join();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        Thread threadJobNotification = getThreadJobNotification();
//        try {
//            threadJobNotification.join();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        new Thread(() -> {
//            while (flagJobNotification) {
//                List<JobNotification> tempNotifications = new ArrayList<>();
//                List<JobUser> existUser = usersList.stream().filter(it -> it.getUser().getUid().equals(uid)).collect(Collectors.toList());
//                if (!existUser.isEmpty()) {
//                    for (JobNotification job : jobsNotificationList) {
//                        Calendar calendarNow = Calendar.getInstance();
//                        int year = calendarNow.get(Calendar.YEAR);
//                        int month = calendarNow.get(Calendar.MONTH);
//                        int dateOfMonth = calendarNow.get(Calendar.DAY_OF_MONTH);
//
//                        Calendar calendarJob = Calendar.getInstance();
//                        calendarJob.setTime(new Date(job.getTimeEnd()));
//
//                        int yearJob = calendarJob.get(Calendar.YEAR);
//                        int monthJob = calendarNow.get(Calendar.MONTH);
//                        int dateOfMonthJob = calendarNow.get(Calendar.DAY_OF_MONTH);
//
//                        boolean isJobNotification = SingletonSharePreferences.getInstance().getJobNotification(job.getJob().getIdJob());
//                        if (year == yearJob && monthJob == month && dateOfMonth == dateOfMonthJob && !isJobNotification) {
//                            tempNotifications.add(job);
//                            createNotification(BackgroundNotificationService.this, job);
//                            SingletonSharePreferences.getInstance().putJobNotification(job.getJob().getIdJob(), true);
//                        }
//                    }
//                    jobsNotificationList.removeAll(tempNotifications);
//
//                }
//            }
//        }).start();
        return START_NOT_STICKY;
    }

    @NonNull
    private Thread getThreadJobNotification() {
        Thread threadJobNotification = new Thread(()->{
            while (flagJobNotification){
                jobsNotificationList.clear();
                for (Job job : jobsList) {
                    FirebaseDatabase.getInstance().getReference("JobNotifications")
                            .child(job.getIdJob())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists() && snapshot.hasChildren()){
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            JobNotificationLocal jobNotification = dataSnapshot.getValue(JobNotificationLocal.class);
                                            if (jobNotification != null) {
                                                jobsNotificationList.add(jobNotification);
                                            }
                                        }
                                        flagJobNotification = !flagJobNotification;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }
            }
        });
        threadJobNotification.start();
        return threadJobNotification;
    }

    @NonNull
    private Thread getThreadJobUser() {
        Thread threadJobUser = new Thread(()->{
            while (flagJobUser) {
                usersList.clear();
                for (Job job : jobsList) {
                    FirebaseDatabase.getInstance().getReference("JobUsers")
                            .child(job.getIdJob()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists() && snapshot.hasChildren()){

                                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                            JobUser jobUser = dataSnapshot.getValue(JobUser.class);
                                            if (jobUser != null) {
                                                usersList.add(jobUser);
                                            }
                                        }
                                        flagJobUser = !flagJobUser;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }
        });
        threadJobUser.start();
        return threadJobUser;
    }

    @NonNull
    private Thread getThread() {
        Thread thread = new Thread(()->{

            while (flagJob) {
                jobsList.clear();
                for (Project project : projectsList) {
                    FirebaseDatabase.getInstance().getReference("Jobs")
                            .child(project.getIdProject())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists() && snapshot.hasChildren()){
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            Job job = dataSnapshot.getValue(Job.class);
                                            if (job != null) {
                                                jobsList.add(job);
                                            }
                                        }
                                        flagJob = !flagJob;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }

            }
        });
        thread.start();
        return thread;
    }

    void createNotification(Context context, JobNotificationLocal jobNotification) {
        // Build the notification and add the action.
        Notification newMessageNotification = new Notification.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_circle_notifications_24)
                .setContentTitle("Thông báo")
                .setContentText(jobNotification.getNameJob())
                .build();

        // Issue the notification.
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(count, newMessageNotification);
        count++;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

package com.theodoilamviec.Account.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theodoilamviec.Account.Job;
import com.theodoilamviec.Account.JobNotificationLocal;
import com.theodoilamviec.Account.TempNotificationObject;
import com.theodoilamviec.Account.listeners.NotificationListener;
import com.theodoilamviec.theodoilamviec.R;
import com.theodoilamviec.Account.adapters.NotificationsAdapter;
import com.theodoilamviec.theodoilamviec.DB.APP_DATABASE;
import com.theodoilamviec.theodoilamviec.DB.DAO;
import com.theodoilamviec.theodoilamviec.models.Notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationsActivity extends AppCompatActivity implements NotificationListener {

    // adapter & notification model
    private NotificationsAdapter adapter;
    private List<Notification> notifications;

    private final List<Job> jobsList = new ArrayList<>();
    private final List<JobNotificationLocal> jobNotifications = new ArrayList<>();

    private final List<TempNotificationObject> listNotifications = new ArrayList<>();
    private DAO dao;

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_notifications);

        // initialize database
        dao = APP_DATABASE.requestDatabase(this).dao();

        // return back and finish activity
        ImageView goBack = findViewById(R.id.go_back);
        goBack.setOnClickListener(v -> finish());

        // delete all notifications
        ImageView deleteAllNotifications = findViewById(R.id.delete_all_notifications);
        deleteAllNotifications.setOnClickListener(v -> {
            dao.requestDeleteAllNotification();
            adapter.submitList(new ArrayList<>());
            deleteAllNoti();
        });

        // notifications list initialize
        RecyclerView recyclerView = findViewById(R.id.notifications_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // notifications list, adapter
        notifications = new ArrayList<>();
        adapter = new NotificationsAdapter(this);
        recyclerView.setAdapter(adapter);

        requestNotifications();
    }

    private void deleteAllNoti() {
        FirebaseDatabase.getInstance().getReference("Notifications").removeValue();
    }

    /**
     * request notifications from
     * DAO, notifications entity
     */
    private void requestNotifications() {
        FirebaseDatabase.getInstance().getReference("Notifications")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists() && snapshot.hasChildren()) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.getKey() == null) continue;
                                FirebaseDatabase.getInstance().getReference("Notifications")
                                        .child(dataSnapshot.getKey())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists() && snapshot.hasChildren()) {
                                                    listNotifications.clear();
                                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                                        try {
                                                            TempNotificationObject tempNotificationObject = dataSnapshot1.getValue(TempNotificationObject.class);
                                                            if (tempNotificationObject != null) {
                                                                listNotifications.add(tempNotificationObject);
                                                            }
                                                        } catch (Exception e) {
                                                            System.out.println("e = " + e.getMessage());
                                                        }
                                                    }
                                                    adapter.submitList(listNotifications);

                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onNotificationClicked(TempNotificationObject notification, int position) {

    }
}
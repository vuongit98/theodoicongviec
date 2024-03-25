package com.theodoilamviec.Account.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.theodoilamviec.Account.Job;
import com.theodoilamviec.Account.JobDocument;
import com.theodoilamviec.Account.JobDocumentLocal;
import com.theodoilamviec.Account.JobLocal;
import com.theodoilamviec.Account.JobNotification;
import com.theodoilamviec.Account.JobNotificationLocal;
import com.theodoilamviec.Account.JobUser;
import com.theodoilamviec.Account.JobUserLocal;
import com.theodoilamviec.Account.PermissionJob;
import com.theodoilamviec.Account.PermissionJobLocal;
import com.theodoilamviec.Account.StaticJobActivity;
import com.theodoilamviec.Account.TimeJobActivity;
import com.theodoilamviec.Account.adapters.ProjectActivity;
import com.theodoilamviec.ISyncData;
import com.theodoilamviec.Project;
import com.theodoilamviec.ProjectLocal;
import com.theodoilamviec.SyncDataManager;
import com.theodoilamviec.theodoilamviec.DB.APP_DATABASE;
import com.theodoilamviec.theodoilamviec.DB.DAO;
import com.theodoilamviec.theodoilamviec.Menu.ReminderActivity;
import com.theodoilamviec.theodoilamviec.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    // Drawer Layout

    // toolbar buttons
    ActivityMainBinding mainBinding;
    SyncDataManager syncDataManager;
    DAO dao;
    List<Job> jobsList = new ArrayList<>();

    @SuppressLint({"WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        dao = APP_DATABASE.requestDatabase(this).dao();

        mainBinding.ghichu.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProjectActivity.class)));
        mainBinding.thungrac.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TrashActivity.class)));
        mainBinding.nhacnho.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ReminderActivity.class)));

        mainBinding.caidat.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SettingsActivity.class)));
        mainBinding.thongbao.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NotificationsActivity.class)));
        mainBinding.phonghop.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, GroupChatActivity.class)));
        mainBinding.tiendo.setOnClickListener(e -> startActivity(new Intent(MainActivity.this, StaticJobActivity.class)));

        mainBinding.dongbo.setOnClickListener(e -> {
        });

        mainBinding.lichlamviec.setOnClickListener(e -> {
            startActivity(new Intent(MainActivity.this, TimeJobActivity.class));
        });
    }
}

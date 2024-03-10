package com.theodoilamviec.Account.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.theodoilamviec.theodoilamviec.Menu.HomeActivity;
import com.theodoilamviec.theodoilamviec.Menu.ReminderActivity;
import com.theodoilamviec.theodoilamviec.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity  {

    // Drawer Layout

    // toolbar buttons
    ActivityMainBinding mainBinding;

    @SuppressLint({"WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        mainBinding.ghichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CreateJobPersonActivity.class));
            }
        });
        mainBinding.thungrac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,TrashActivity.class));
            }
        });
        mainBinding.nhacnho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ReminderActivity.class));
            }
        });

        mainBinding.caidat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
            }
        });
        mainBinding.thongbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,NotificationsActivity.class));
            }
        });

        mainBinding.phonghop.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,GroupChatActivity.class));
        });




    }








}

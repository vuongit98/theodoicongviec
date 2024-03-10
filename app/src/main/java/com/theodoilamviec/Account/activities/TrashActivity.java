package com.theodoilamviec.Account.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.theodoilamviec.Account.fragments.TrashFragment;
import com.theodoilamviec.theodoilamviec.R;

public class TrashActivity   extends AppCompatActivity {
    public static Button extraAction;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);
        extraAction = findViewById(R.id.extra_action);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thùng Rác");


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TrashFragment()).commit();

    }

}

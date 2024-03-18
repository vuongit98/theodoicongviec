package com.theodoilamviec.theodoilamviec.Menu;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.theodoilamviec.Account.Job;
import com.theodoilamviec.Account.JobNotificationLocal;
import com.theodoilamviec.Account.adapters.ReminderNotesAdapter;
import com.theodoilamviec.BroadcastReminder;
import com.theodoilamviec.Project;
import com.theodoilamviec.ReminderHandleData;
import com.theodoilamviec.theodoilamviec.DB.APP_DATABASE;
import com.theodoilamviec.theodoilamviec.databinding.ActivityReminderBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReminderActivity extends AppCompatActivity implements ReminderHandleData.IGetDataReminder, ReminderNotesAdapter.IClickReminderNotes {

    ActivityReminderBinding reminderBinding ;
    ReminderNotesAdapter reminderNotesAdapter;
    private final List<Project> projectsList = new ArrayList<>();

    private final ReminderHandleData reminderHandleData = new ReminderHandleData();

    AlarmManager alarmManager ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reminderBinding = ActivityReminderBinding.inflate(getLayoutInflater());
        setContentView(reminderBinding.getRoot());

        setSupportActionBar(reminderBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Nhắc nhở");

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        reminderBinding.toolbar.setNavigationOnClickListener(v -> finish());

        reminderBinding.notesRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        reminderNotesAdapter = new ReminderNotesAdapter(this);
        reminderBinding.notesRecyclerview.setAdapter(reminderNotesAdapter);
        List<JobNotificationLocal> jobNotificationList = APP_DATABASE.requestDatabase(this).dao().requestAllJobNotificationLocal();
        reminderNotesAdapter.submitList(jobNotificationList);
    }

    @Override
    public void getProjectList(List<Project> projectList) {
        projectsList.clear();
        projectsList.addAll(projectList);
        reminderHandleData.getJobsList(projectList,this);
    }

    @Override
    public void getJobsList(List<Job> jobsList) {
        reminderHandleData.getJobsList(projectsList,this);

    }

    @Override
    public void getJobNotificationList(List<JobNotificationLocal> jobNotificationList) {
        reminderHandleData.getJobNotificationList(this);
    }

    @Override
    public void getReminderNote(JobNotificationLocal jobNotificationLocal) {
        @SuppressLint("ScheduleExactAlarm") AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Nhắc nhở")
                .setMessage("Bạn có muốn thông báo nhắc nhở hay không ?")
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setPositiveButton("Ok", (dialog, which) -> {

                    Intent intent = new Intent(this, BroadcastReminder.class);
                    intent.putExtra("NOTIFICATION","1");
                    intent.putExtra("name_project", jobNotificationLocal.getNameJob());
                    intent.putExtra("id_project", jobNotificationLocal.getIdProject());
                    intent.putExtra("id_job", jobNotificationLocal.getIdJob());

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,jobNotificationLocal.timeEnd,pendingIntent);

                    dialog.dismiss();
                }).create();

        alertDialog.show();
    }
}

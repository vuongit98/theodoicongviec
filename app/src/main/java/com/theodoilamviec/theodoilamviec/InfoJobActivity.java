package com.theodoilamviec.theodoilamviec;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theodoilamviec.Account.Job;
import com.theodoilamviec.Account.JobDocument;
import com.theodoilamviec.Account.JobUser;
import com.theodoilamviec.Account.PermissionJob;
import com.theodoilamviec.Account.adapters.FileAttachedAdapter;
import com.theodoilamviec.Account.adapters.ProjectActivity;
import com.theodoilamviec.theodoilamviec.Menu.HomeActivity;
import com.theodoilamviec.theodoilamviec.databinding.ActivityInfoJobBinding;
import com.theodoilamviec.theodoilamviec.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class InfoJobActivity extends AppCompatActivity implements FileAttachedAdapter.IClickFileAttached {

    ActivityInfoJobBinding binding;
    Job job;
    String uidCurrent = FirebaseAuth.getInstance().getUid();
    List<JobUser> jobUsersList = new ArrayList<>();
    List<JobUser> previousJobUsersList = new ArrayList<>();
    List<JobDocument> jobDocumentsList = new ArrayList<>();
    List<JobDocument> previousJobDocumentsList = new ArrayList<>();
    List<String> uidPermissionList = new ArrayList<>();

    FileAttachedAdapter fileAttachedAdapter;

    int permissionEdit = 0;
    Long timeStartPicker = 0L;
    Long timeEndPicker = 0L;
    int highPriority = 0;
    int statusJob = 0;
    String[] priorityList = new String[]{"New", "Medium", "Urgent"};
    String[] statusJobList = new String[]{"New", "Responding", "Finished"};


    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoJobBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        fileAttachedAdapter = new FileAttachedAdapter(this, this);
        fileAttachedAdapter.setShowFullScreen(true);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                job = bundle.getParcelable("job", Job.class);
            } else {
                job = bundle.getParcelable("job");
            }
        }
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        calendar = Calendar.getInstance();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, priorityList);
        binding.spPriority.setAdapter(spinnerAdapter);

        ArrayAdapter<String> spinnerStatusJobAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, statusJobList);
        binding.spStatusJob.setAdapter(spinnerStatusJobAdapter);

        binding.spPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                highPriority = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spStatusJob.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusJob = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(this, ProjectActivity.class)));

        if (job != null) {
            getSupportActionBar().setTitle(job.getNameJob());
            binding.edtNameJob.setText(job.getNameJob());
            binding.spPriority.setSelection(job.getHighPriority());
            binding.spStatusJob.setSelection(job.getStatusJob());
            highPriority = job.getHighPriority();
            statusJob = job.getStatusJob();
            calendar.setTime(new Date(job.getTimeStartDate()));
            int[] timeJob = getTimeJob(calendar);
            String timeStart = timeJob[2] + "/" + timeJob[1] + "/" + timeJob[0];
            binding.tvStartDatePicker.setText(timeStart);
            calendar.setTime(new Date(job.getTimeEndDate()));
            timeJob = getTimeJob(calendar);
            String timeEnd = timeJob[2] + "/" + timeJob[1] + "/" + timeJob[0];
            binding.tvEndDatePicker.setText(timeEnd);
            getListPermissionJobs(job.getIdJob());
            getJobUserById(job.getIdJob());
            getJobDocumentsById(job.getIdJob());
            binding.edtNameJob.setEnabled(false);
            binding.tvEndDatePicker.setEnabled(false);
            binding.tvStartDatePicker.setEnabled(false);
            binding.spPriority.setEnabled(false);
            binding.spStatusJob.setEnabled(false);
            binding.ivAttachFile.setEnabled(false);
            binding.ivAttachPeople.setEnabled(false);

        }

        binding.tvStartDatePicker.setOnClickListener(e -> {
            showDialogTime(true);

        });

        binding.tvEndDatePicker.setOnClickListener(e -> {
            showDialogTime(false);

        });

        binding.rcvFileAttached.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rcvFileAttached.addItemDecoration(new GridSpacingItemDecoration(2, 10, true));
        binding.rcvFileAttached.setAdapter(fileAttachedAdapter);

    }

    int[] getTimeJob(Calendar calendar) {
        int yearStart = calendar.get(Calendar.YEAR);
        int monthStart = calendar.get(Calendar.MONTH);
        int dateOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return new int[]{yearStart, monthStart, dateOfMonth};
    }

    private void showDialogTime(Boolean isStarted) {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, i, i1, i2) -> {
            Calendar getTime = Calendar.getInstance();
            getTime.set(i, i1, i2);
            if (isStarted) {
                timeStartPicker = getTime.getTimeInMillis();
                binding.tvStartDatePicker.setText(i2 + "/" + i1 + "/" + i);
            } else {
                timeEndPicker = getTime.getTimeInMillis();
                binding.tvEndDatePicker.setText(i2 + "/" + i1 + "/" + i);

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void getJobUserById(String idJob) {
        FirebaseDatabase.getInstance().getReference("JobUsers").
                child(job.getIdProject()).
                child(idJob).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.hasChildren()) {
                            List<String> stringUserList = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                JobUser jobUser = dataSnapshot.getValue(JobUser.class);
                                if (jobUser != null) {
                                    jobUsersList.add(jobUser);
                                    previousJobUsersList.add(jobUser);
                                    String nameUser = jobUser.getUser().getUserName();
                                    stringUserList.add(nameUser.substring(0, nameUser.indexOf("@gmail")));
                                }
                            }
                            String usersList = String.join(", ", stringUserList);
                            binding.tvNamePerson.setText(usersList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    public void getListPermissionJobs(String idJob) {
        FirebaseDatabase.getInstance().getReference("Permissions")
                .child(job.getIdProject())
                .child(idJob)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists() && snapshot.hasChildren()) {
                            uidPermissionList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                PermissionJob permissionJob = dataSnapshot.getValue(PermissionJob.class);
                                if (permissionJob != null) {
                                    uidPermissionList.add(permissionJob.getUid());
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void getJobDocumentsById(String idJob) {
        FirebaseDatabase.getInstance().getReference("JobDocuments")
                .child(job.getIdProject())
                .child(idJob)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.hasChildren()) {
                            jobDocumentsList.clear();
                            previousJobDocumentsList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                JobDocument jobDocument = dataSnapshot.getValue(JobDocument.class);
                                if (jobDocument != null) {
                                    jobDocumentsList.add(jobDocument);
                                    previousJobDocumentsList.add(jobDocument);
                                }
                            }
                            fileAttachedAdapter.submitList(jobDocumentsList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.item_right_toolbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_job) {
            if (uidPermissionList.contains(uidCurrent)) {
                deleteJob(job.getIdJob());
                startActivity(new Intent(InfoJobActivity.this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Không có quyền xóa.", Toast.LENGTH_SHORT).show();
            }
        } else if (item.getItemId() == R.id.edit_job) {


            if (permissionEdit == 0) {
                if (uidPermissionList.contains(uidCurrent)) {
                    permissionEdit = 1;
                    binding.edtNameJob.setEnabled(true);
                    binding.tvEndDatePicker.setEnabled(true);
                    binding.tvStartDatePicker.setEnabled(true);
                    binding.spPriority.setEnabled(true);
                    binding.spStatusJob.setEnabled(true);
                    binding.ivAttachFile.setEnabled(true);
                    binding.ivAttachPeople.setEnabled(true);

                    item.setIcon(R.drawable.icon_category);
                } else {
                    Toast.makeText(this, "Không có quyền chỉnh sửa", Toast.LENGTH_SHORT).show();
                }
            } else {
                job.setNameJob(binding.edtNameJob.getText().toString().trim());
                job.setHighPriority(highPriority);
                job.setTimeStartDate(timeStartPicker);
                job.setTimeEndDate(timeEndPicker);
                job.setStatusJob(statusJob);
                saveJob(job);
                item.setIcon(R.drawable.baseline_edit_24);
                permissionEdit = 0;
            }
        }
        return true;
    }

    void deleteJob(String idJob) {
        FirebaseDatabase.getInstance().getReference("JobUsers").child(job.getIdProject()).child(idJob).removeValue();
        FirebaseDatabase.getInstance().getReference("JobDocuments").child(job.getIdProject()).child(idJob).removeValue();
        FirebaseDatabase.getInstance().getReference("Jobs").child(job.getIdProject()).child(idJob).removeValue();
    }

    void saveJob(Job job) {
        FirebaseDatabase.getInstance().getReference("Jobs")
                .child(job.getIdProject())
                .child(job.getIdJob())
                .setValue(job);

        if (!new HashSet<>(jobDocumentsList).containsAll(previousJobDocumentsList)) {
            FirebaseDatabase.getInstance().getReference("JobDocuments").child(job.getIdJob()).removeValue();
            for (JobDocument jobDocument : jobDocumentsList) {
                FirebaseDatabase.getInstance().getReference("JobDocuments")
                        .child(job.getIdProject())
                        .child(jobDocument.getIdJob())
                        .child(jobDocument.getIdJobDocument()).setValue(jobDocument);
            }
        }
        if (!new HashSet<>(jobUsersList).containsAll(previousJobUsersList)) {
            FirebaseDatabase.getInstance().getReference("JobUsers")
                    .child(job.getIdProject())
                    .child(job.getIdJob()).removeValue();
            for (JobUser jobUser : jobUsersList) {
                FirebaseDatabase.getInstance().getReference("JobUsers")
                        .child(job.getIdProject())
                        .child(jobUser.getIdJob())
                        .child(jobUser.getIdJobUser()).setValue(jobUser);
            }
        }
    }

    @Override
    public void deleteFile(JobDocument jobDocument) {
        jobDocumentsList.remove(jobDocument);
        fileAttachedAdapter.submitList(jobDocumentsList);
    }

    @Override
    public void getFile(JobDocument jobDocument) {

    }
}
package com.theodoilamviec.theodoilamviec.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theodoilamviec.Account.Job;
import com.theodoilamviec.Account.activities.CreateJobPersonActivity;
import com.theodoilamviec.Account.adapters.ProjectActivity;
import com.theodoilamviec.theodoilamviec.InfoJobActivity;
import com.theodoilamviec.theodoilamviec.R;
import com.theodoilamviec.theodoilamviec.adapter.JobAdapter;
import com.theodoilamviec.theodoilamviec.databinding.ActivityHomeBinding;
import com.theodoilamviec.theodoilamviec.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeActivity extends AppCompatActivity implements JobAdapter.IClickJob {
    int sort = 0;
    JobAdapter jobAdapter;
    ActivityHomeBinding binding;

    List<Job> jobsList = new ArrayList<>();
    String idProject = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);

        Intent intent = getIntent();

        idProject = intent.getStringExtra("id_project");
        jobAdapter = new JobAdapter(this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Job");
        toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ProjectActivity.class)));

        binding.rcvProject.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rcvProject.addItemDecoration(new GridSpacingItemDecoration(2, 10, true));
        binding.rcvProject.setAdapter(jobAdapter);
        getJobsList();

        binding.fbtAddJob.setOnClickListener(e -> {
            Intent intentHome = new Intent(this, CreateJobPersonActivity.class);
            intentHome.putExtra("id_project", idProject);
            startActivity(intentHome);
        });

    }

    public void getJobsList() {
        FirebaseDatabase.getInstance().getReference("Jobs")
                .child(idProject)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.hasChildren()) {
                            binding.progressBar.setVisibility(View.VISIBLE);
                            jobsList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Job job = dataSnapshot.getValue(Job.class);
                                if (job != null) {
                                    if (!job.isDeleted()) {
                                        jobsList.add(job);
                                    }
                                }
                            }
                            jobAdapter.submitList(jobsList);
                            binding.progressBar.setVisibility(View.GONE);
                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("onCancelled: ", "Error = " + error.getMessage());

                    }
                });
    }

    @Override
    public void getJob(Job job) {
        Intent intentJob = new Intent(this, InfoJobActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("job", job);
        intentJob.putExtra("bundle",bundle);
        startActivity(intentJob);
    }
}

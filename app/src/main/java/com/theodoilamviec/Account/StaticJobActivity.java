package com.theodoilamviec.Account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theodoilamviec.Account.activities.MainActivity;
import com.theodoilamviec.Account.adapters.ProjectActivity;
import com.theodoilamviec.Project;
import com.theodoilamviec.theodoilamviec.Menu.HomeActivity;
import com.theodoilamviec.theodoilamviec.databinding.ActivityStaticJobBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class StaticJobActivity extends AppCompatActivity implements StaticJobUtils.IStatusJob {

    ActivityStaticJobBinding binding ;
    StaticJobUtils staticJobUtils ;
    String uid = FirebaseAuth.getInstance().getUid();
    String []xData = new String[]{"Completed","Uncompleted","Responding"};

    List<Project> projectsList = new ArrayList<>();
    String[] projectStringArray = new String[10];

    ArrayAdapter<String> arrayAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStaticJobBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        staticJobUtils = new StaticJobUtils();
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thống kê trạng thái công việc");
        binding.toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(StaticJobActivity.this, MainActivity.class)));
        staticJobUtils.getProjects(this);
        binding.spinnerProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                staticJobUtils.getJobStatus(projectsList.get(position).getIdProject(),StaticJobActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }



    @Override
    public void getStatusJobList(List<Job> completedJobsList, List<Job> unCompletedJobsList, List<Job> respondingJobsList) {
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();

        dataEntries.add(new ValueDataEntry("Completed", completedJobsList.size()));
        dataEntries.add(new ValueDataEntry("Uncompleted", unCompletedJobsList.size()));
        dataEntries.add(new ValueDataEntry("Responding", respondingJobsList.size()));

        pie.data(dataEntries);
        pie.title("Status Job");
        binding.pieChart.setChart(pie);
    }

    @Override
    public void getProjectList(HashMap<String,Project> projectList) {
        FirebaseDatabase.getInstance()
                .getReference("PermissionProject")
                .child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        int index =0 ;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String idProject = dataSnapshot.getKey();
                            Project project = projectList.get(idProject);
                            if (project != null) {
                                projectsList.add(project);
                                projectStringArray[index++] = project.getNameProject();
                            }
                        }
                        arrayAdapter = new ArrayAdapter<>(StaticJobActivity.this, android.R.layout.simple_spinner_dropdown_item,projectStringArray);
                        binding.spinnerProject.setAdapter(arrayAdapter);
                        staticJobUtils.getJobStatus(projectsList.get(0).getIdProject(),StaticJobActivity.this);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
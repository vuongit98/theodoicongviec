package com.theodoilamviec.Account;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theodoilamviec.Project;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class StaticJobUtils {

    public interface IStatusJob {
        void getStatusJobList(List<Job> completedJobsList, List<Job> unCompletedJobsList, List<Job> respondingJobsList);
        void getProjectList(HashMap<String , Project> projectList);
    }

    FirebaseDatabase databaseReference = FirebaseDatabase.getInstance();
    private final List<Job> jobsFinshedList = new ArrayList<>();
    private final List<Job> jobsUnCompletedList = new ArrayList<>();
    private final List<Job> jobsRespondingList = new ArrayList<>();

    public void getProjects(IStatusJob iStatusJob) {
        FirebaseDatabase.getInstance()
                .getReference("Projects")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.hasChildren()){
                            HashMap<String, Project> projectList = new HashMap<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Project project = dataSnapshot.getValue(Project.class);
                                if (project != null) {
                                    projectList.put(project.getIdProject(), project);
                                }
                            }
                            iStatusJob.getProjectList(projectList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void getJobStatus(String idProject, IStatusJob iStatusJob) {
        Long timeCurrent = Calendar.getInstance().getTimeInMillis();
        FirebaseDatabase.getInstance().getReference("Jobs")
                .child(idProject)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.hasChildren()) {

                            for (DataSnapshot dataSnapShot : snapshot.getChildren()) {
                                Job job = dataSnapShot.getValue(Job.class);
                                assert job != null;
                                if (job.getTimeEndDate() <= timeCurrent && (job.getStatusJob() != 2)) {
                                    jobsUnCompletedList.add(job);
                                } else if (job.getStatusJob() == 1 || job.getStatusJob() == 0) {
                                    jobsRespondingList.add(job);
                                } else {
                                    {
                                        jobsFinshedList.add(job);
                                    }
                                }
                            }
                            iStatusJob.getStatusJobList(jobsFinshedList, jobsUnCompletedList, jobsRespondingList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}

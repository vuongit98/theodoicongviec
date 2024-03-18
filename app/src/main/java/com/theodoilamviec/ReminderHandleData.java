package com.theodoilamviec;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theodoilamviec.Account.Job;
import com.theodoilamviec.Account.JobNotificationLocal;

import java.util.ArrayList;
import java.util.List;

public class ReminderHandleData {

    public interface IGetDataReminder{
        void getProjectList(List<Project> projectList);
        void getJobsList(List<Job> jobsList);
        void getJobNotificationList(List<JobNotificationLocal> jobNotificationList);
    }
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public void getProject(IGetDataReminder iGetDataReminder) {
        firebaseDatabase.getReference("Project")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.hasChildren()){
                            List<Project> projectList = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Project project = dataSnapshot.getValue(Project.class);
                                if (project != null) {
                                    projectList.add(project);
                                }
                                iGetDataReminder.getProjectList(projectList);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void getJobNotificationList(IGetDataReminder iGetDataReminder) {
        firebaseDatabase.getReference("Jobs")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.hasChildren()){
                            List<JobNotificationLocal> projectList = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                JobNotificationLocal job = dataSnapshot.getValue(JobNotificationLocal.class);
                                if (job != null) {
                                    projectList.add(job);
                                }
                                iGetDataReminder.getJobNotificationList(projectList);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void getJobsList(List<Project> projectList ,IGetDataReminder iGetDataReminder) {
        
        firebaseDatabase.getReference("Jobs")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.hasChildren()){
                            List<Job> projectList = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Job job = dataSnapshot.getValue(Job.class);
                                if (job != null) {
                                    projectList.add(job);
                                }
                                iGetDataReminder.getJobsList(projectList);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}

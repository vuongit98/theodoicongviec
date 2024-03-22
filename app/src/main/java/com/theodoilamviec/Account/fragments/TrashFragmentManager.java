package com.theodoilamviec.Account.fragments;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theodoilamviec.Account.DB.APP_DATABASE;
import com.theodoilamviec.Account.Job;
import com.theodoilamviec.Account.PermissionProject;

import java.util.ArrayList;
import java.util.List;

public class TrashFragmentManager {

    public interface IProjectStatus {
        void loadingData();
        void finishedData();
    }
    public interface IProjectData {
        void getListIdProject(List<String> idProjectList);
        void getListJobsById(List<Job> jobList);
    }

    private IProjectData iProjectData;

    public TrashFragmentManager(IProjectData iProjectData) {
        this.iProjectData = iProjectData;
    }

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String uid = FirebaseAuth.getInstance().getUid();

    public void getAllProject() {

        FirebaseDatabase.getInstance()
                .getReference("PermissionProject")
                .child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.hasChildren()) {
                            List<String> idProjectList = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.getKey() != null) {
                                    idProjectList.add(dataSnapshot.getKey());
                                }
                            }
                            iProjectData.getListIdProject(idProjectList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void getAllJobByIdProject(String idProject) {

        FirebaseDatabase.getInstance().getReference("Jobs")
                .child(idProject)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists() && snapshot.hasChildren()) {
                            List<Job> jobsList = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Job job = dataSnapshot.getValue(Job.class);
                                if (job != null && job.isDeleted()) {
                                    jobsList.add(job);
                                }
                            }
                            iProjectData.getListJobsById(jobsList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    public void deleteAll(List<Job> jobRemovedList){
        for (Job job : jobRemovedList) {

            FirebaseDatabase.getInstance().getReference("Jobs").child(job.getIdProject())
                    .child(job.getIdJob()).removeValue();

            FirebaseDatabase.getInstance().getReference("JobUsers").child(job.getIdProject())
                    .child(job.getIdJob()).removeValue();

            FirebaseDatabase.getInstance().getReference("JobNotifications").child(job.getIdJob()).removeValue();
            FirebaseDatabase.getInstance().getReference("Permissions").child(job.getIdProject())
                    .child(job.getIdJob()).removeValue();

        }
    }
}

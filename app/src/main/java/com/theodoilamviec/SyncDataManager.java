package com.theodoilamviec;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theodoilamviec.Account.Job;
import com.theodoilamviec.Account.JobDocument;
import com.theodoilamviec.Account.JobNotification;
import com.theodoilamviec.Account.JobUser;
import com.theodoilamviec.Account.PermissionJob;

import java.util.ArrayList;
import java.util.List;

public class SyncDataManager {

    private final ISyncData iSyncData;

    private final List<Project> projectsList = new ArrayList<>();
    private final List<Job> jobsList = new ArrayList<>();
    private final List<JobNotification> jobNotificationList = new ArrayList<>();
    private final List<JobDocument> jobDocumentList = new ArrayList<>();
    private final List<JobUser> jobUsersList = new ArrayList<>();
    private final List<PermissionJob> permissionJobList = new ArrayList<>();

    public SyncDataManager(ISyncData syncData) {
        iSyncData = syncData;
    }

    public void syncDataProjects() {
        FirebaseDatabase.getInstance().getReference("Project")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.hasChildren()) {
                            List<Project> projectsList = new ArrayList<>();
                            for (DataSnapshot dataSnap : snapshot.getChildren()) {
                                Project project = dataSnap.getValue(Project.class);
                                if (project != null) {
                                    projectsList.add(project);
                                }
                            }
                            iSyncData.getSyncProject(projectsList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void syncDataJobs(List<Project> projectList) {
        jobsList.clear();
        for (Project project : projectList) {
            FirebaseDatabase.getInstance().getReference("Jobs")
                    .child(project.getIdProject())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists() && snapshot.hasChildren()) {

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Job job = dataSnapshot.getValue(Job.class);
                                    if (job != null) {
                                        jobsList.add(job);
                                    }
                                }
                                iSyncData.getSyncJobs(jobsList);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    public void sysDataJobNotification(List<Job> jobsList) {
        jobNotificationList.clear();
        for (Job job : jobsList) {
            FirebaseDatabase.getInstance().getReference("JobNotifications")
                    .child(job.getIdJob())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists() && snapshot.hasChildren()) {

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    JobNotification jobNotification = dataSnapshot.getValue(JobNotification.class);
                                    if (jobNotification != null) {
                                        jobNotificationList.add(jobNotification);
                                    }
                                }
                                iSyncData.getSyncJobNotifications(jobNotificationList);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    public void sysDataJobDocument(List<Job> jobsList) {
        jobDocumentList.clear();
        for (Job job : jobsList) {
            FirebaseDatabase.getInstance().getReference("JobDocuments")
                    .child(job.getIdJob())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists() && snapshot.hasChildren()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    JobDocument jobDocument = dataSnapshot.getValue(JobDocument.class);
                                    if (jobDocument != null) {
                                        jobDocumentList.add(jobDocument);
                                    }
                                }
                                iSyncData.getSyncJobDocuments(jobDocumentList);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    public void sysDataJobUser(List<Job> jobsList) {
        jobUsersList.clear();
        for (Job job : jobsList) {
            FirebaseDatabase.getInstance().getReference("JobUsers")
                    .child(job.getIdJob())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists() && snapshot.hasChildren()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    JobUser jobUser = dataSnapshot.getValue(JobUser.class);
                                    if (jobUser != null) {
                                        jobUsersList.add(jobUser);
                                    }
                                }
                                iSyncData.getSyncJobUser(jobUsersList);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    public void sysDataPermissionJob(List<Job> jobsList) {
        permissionJobList.clear();
        for (Job job : jobsList) {

            FirebaseDatabase.getInstance().getReference("PermissionJob")
                    .child(job.getIdJob())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists() && snapshot.hasChildren()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    PermissionJob permissionJob = dataSnapshot.getValue(PermissionJob.class);
                                    if (permissionJob != null) {
                                        permissionJobList.add(permissionJob);
                                    }
                                }
                                iSyncData.getSyncPermissionJob(permissionJobList);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }
}

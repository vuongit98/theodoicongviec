package com.theodoilamviec.Account;

import androidx.annotation.NonNull;

import com.google.android.exoplayer2.C;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TimeJobManager {

    public interface ITimeJob {
        void getIdProjectList(List<String> idProjectList);

        void getListJobByTime(HashMap<String, List<Job>> dataTimJobMap);
    }

    private ITimeJob iTimeJob;

    public TimeJobManager(ITimeJob iTimeJob) {
        this.iTimeJob = iTimeJob;
    }

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final String uid = FirebaseAuth.getInstance().getUid();


    public void getListJobByTime(String idProject) {
        firebaseDatabase.getReference("Jobs").child(idProject).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChildren()) {
                    HashMap<String, List<Job>> dataTimeJobMap = new HashMap<>();
                    Calendar calendar = Calendar.getInstance();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Job job = dataSnapshot.getValue(Job.class);
                        if (job != null) {
                            calendar.setTime(new Date(job.getTimeStartDate()));

                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH);
                            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                           List<Job> jobList = dataTimeJobMap.get(month+"/"+dayOfMonth+"/"+year);
                           if (jobList == null) jobList = new ArrayList<>();
                           jobList.add(job);
                           dataTimeJobMap.put(month+"/"+dayOfMonth+"/"+year, jobList);
                        }
                    }
                    iTimeJob.getListJobByTime(dataTimeJobMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getIdProjectList() {
        firebaseDatabase.getReference("PermissionProject")
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
                            iTimeJob.getIdProjectList(idProjectList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}

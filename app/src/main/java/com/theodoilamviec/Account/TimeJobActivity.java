package com.theodoilamviec.Account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.theodoilamviec.theodoilamviec.InfoJobActivity;
import com.theodoilamviec.theodoilamviec.adapter.JobAdapter;
import com.theodoilamviec.theodoilamviec.databinding.ActivityTimeJobBinding;
import com.theodoilamviec.theodoilamviec.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;


public class TimeJobActivity extends AppCompatActivity implements TimeJobManager.ITimeJob, JobAdapter.IClickJob {

    ActivityTimeJobBinding binding;
    TimeJobManager timeJobManager;
    HashMap<Long, List<Job> > dataMap = new HashMap<>();
    JobAdapter jobAdapter ;
    Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimeJobBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lịch công việc");

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        timeJobManager = new TimeJobManager(this);
        binding.tvTitle.setVisibility(View.GONE);
        timeJobManager.getIdProjectList();
        jobAdapter = new JobAdapter(this, this,false);
        binding.rcvItemJob.setLayoutManager(new GridLayoutManager(this,2));
        binding.rcvItemJob.addItemDecoration(new GridSpacingItemDecoration(2,10, true));
        binding.rcvItemJob.setAdapter(jobAdapter);
        binding.viewCalender.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                calendar.set(date.getYear(), date.getMonth(), date.getDay());
                binding.tvTitle.setVisibility(View.VISIBLE);
                List<Job> jobList = dataMap.getOrDefault(calendar.getTimeInMillis(), new ArrayList<>());
                if (jobList != null && jobList.isEmpty()){
                    binding.rcvItemJob.setVisibility(View.GONE);
                    binding.tvError.setVisibility(View.VISIBLE);
                }else {
                    if (jobList != null) {
                        binding.tvError.setVisibility(View.GONE);
                        binding.rcvItemJob.setVisibility(View.VISIBLE);
                        jobAdapter.submitList(jobList);
                    }
                }
            }
        });
    }

    @Override
    public void getIdProjectList(List<String> idProjectList) {
        idProjectList.forEach(it ->
                timeJobManager.getListJobByTime(it)
        );
    }

    @Override
    public void getListJobByTime(HashMap<Long, List<Job>> dataTimJobMap) {
        dataMap.putAll(dataTimJobMap);
        for (Long it : dataMap.keySet()) {
            calendar.setTime(new Date(it));
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            binding.viewCalender.markDate(year,month,dayOfMonth);
        }
    }

    @Override
    public void getJob(Job job) {
        Intent intent = new Intent(TimeJobActivity.this, InfoJobActivity.class);
        intent.putExtra("isMenu", 1);
        Bundle bundle = new Bundle();
        bundle.putParcelable("job", job);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    @Override
    public void removeJob(Job job) {

    }
}
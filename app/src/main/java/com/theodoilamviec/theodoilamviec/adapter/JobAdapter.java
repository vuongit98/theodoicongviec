package com.theodoilamviec.theodoilamviec.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.theodoilamviec.Account.Job;
import com.theodoilamviec.theodoilamviec.R;
import com.theodoilamviec.theodoilamviec.databinding.ItemCardProjectBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {


    public interface IClickJob {
        void getJob(Job job);
        void removeJob(Job job);
    }

    private final List<Job> jobsList = new ArrayList<>();
    private final Context mContext;

    private final IClickJob iClickJob;
    private Boolean isRecovery = false ;

    public JobAdapter(IClickJob iClickJob, Context context, Boolean isRecovery) {
        this.iClickJob = iClickJob;
        this.mContext = context;
        this.isRecovery = isRecovery;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<Job> data) {
        jobsList.clear();
        jobsList.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JobViewHolder(ItemCardProjectBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobsList.get(position);
        holder.setJob(job, iClickJob,mContext,isRecovery);
        if (isRecovery) {
            holder.binding.layoutJob.setOnLongClickListener(v -> {
                @SuppressLint("NotifyDataSetChanged") AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                        .setTitle("Khôi phục")
                        .setMessage("Bạn có muốn khôi phục công việc " + job.getNameJob() + " hay không?")
                        .setNegativeButton("Cancel", ((dialog, which) -> {
                            dialog.dismiss();
                        }))
                        .setPositiveButton("Ok", ((dialog, which) -> {
                            FirebaseDatabase.getInstance().getReference("Jobs")
                                    .child(job.getIdProject())
                                    .child(job.getIdJob())
                                    .child("deleted")
                                    .setValue(false);
                            iClickJob.removeJob(job);
                            dialog.dismiss();
                        })).create();
                alertDialog.show();
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return jobsList.size();
    }

    static class JobViewHolder extends RecyclerView.ViewHolder {
        ItemCardProjectBinding binding;

        public JobViewHolder(@NonNull ItemCardProjectBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        Calendar calendar = Calendar.getInstance();

        @SuppressLint("SetTextI18n")
        void setJob(Job job, IClickJob iClickJob, Context context, Boolean isRecovery) {
            binding.tvNameJob.setText(job.getNameJob());
            calendar.setTime(new Date(job.getTimeStartDate()));

            int[] timeJob = getTimeJob(calendar);
            String timeStart = "Bắt đầu: " + timeJob[2] + "/" + timeJob[1] + "/" + timeJob[0];
            calendar.setTime(new Date(job.getTimeEndDate()));

            timeJob = getTimeJob(calendar);
            String timeEnd = "Kết thúc: " + timeJob[2] + "/" + timeJob[1] + "/" + timeJob[0];
            binding.tvTimeJob.setText(timeStart + "\n" + timeEnd);

            binding.layoutJob.setOnClickListener(e -> iClickJob.getJob(job));
            Glide.with(context).load(job.getUrlImage()).error(R.drawable.businessman).into(binding.imgJob);

        }

        int[] getTimeJob(Calendar calendar) {
            int yearStart = calendar.get(Calendar.YEAR);
            int monthStart = calendar.get(Calendar.MONTH);
            int dateOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            return new int[]{yearStart, monthStart, dateOfMonth};
        }
    }
}

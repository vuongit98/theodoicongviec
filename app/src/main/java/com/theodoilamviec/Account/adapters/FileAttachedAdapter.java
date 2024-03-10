package com.theodoilamviec.Account.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theodoilamviec.Account.JobDocument;
import com.theodoilamviec.theodoilamviec.databinding.ItemLayoutFileAttachedBinding;

import java.util.ArrayList;
import java.util.List;

public class FileAttachedAdapter extends RecyclerView.Adapter<FileAttachedAdapter.FileViewHolder> {

    private final List<JobDocument> jobDocumentsList = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<JobDocument> data){
        jobDocumentsList.clear();
        jobDocumentsList.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileViewHolder(ItemLayoutFileAttachedBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        JobDocument jobDocument = jobDocumentsList.get(position);
        holder.setFileAttached(jobDocument);
    }

    @Override
    public int getItemCount() {
        return jobDocumentsList.size();
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {

        ItemLayoutFileAttachedBinding binding ;
        public FileViewHolder(@NonNull ItemLayoutFileAttachedBinding itemView) {
            super(itemView.getRoot());
            binding = itemView ;
        }

        public void setFileAttached(JobDocument document) {
            binding.tvNameFile.setText(document.getNameDocument());
        }
    }
}

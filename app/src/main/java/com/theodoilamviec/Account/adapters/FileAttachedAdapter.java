package com.theodoilamviec.Account.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.theodoilamviec.Account.JobDocument;
import com.theodoilamviec.theodoilamviec.R;
import com.theodoilamviec.theodoilamviec.databinding.ItemLayoutFileAttachedBinding;

import java.util.ArrayList;
import java.util.List;

public class FileAttachedAdapter extends RecyclerView.Adapter<FileAttachedAdapter.FileViewHolder> {

    public interface IClickFileAttached {
        void deleteFile(JobDocument jobDocument);
        void getFile(JobDocument jobDocument);
    }
    private final List<JobDocument> jobDocumentsList = new ArrayList<>();
    private Boolean isShowFullScreen = false;
    private Context context ;
    private final IClickFileAttached iClickFileAttached ;
    public FileAttachedAdapter(Context context, IClickFileAttached iClickFileAttached){
        this.context = context ;
        this.iClickFileAttached = iClickFileAttached;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<JobDocument> data) {
        jobDocumentsList.clear();
        jobDocumentsList.addAll(data);
        notifyDataSetChanged();
    }
    public void setShowFullScreen(Boolean data) {
        isShowFullScreen = data;
    }
    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileViewHolder(ItemLayoutFileAttachedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        JobDocument jobDocument = jobDocumentsList.get(position);
        holder.setFileAttached(jobDocument, isShowFullScreen,context,iClickFileAttached);
    }
    @Override
    public int getItemCount() {
        return jobDocumentsList.size();
    }
    static class FileViewHolder extends RecyclerView.ViewHolder {
        ItemLayoutFileAttachedBinding binding;
        public FileViewHolder(@NonNull ItemLayoutFileAttachedBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
        public void setFileAttached(JobDocument document,
                                    Boolean isShowFullDocument,
                                    Context context, IClickFileAttached iClickFileAttached) {
            if (!isShowFullDocument) {
                binding.ivDocument.setVisibility(View.GONE);
                binding.tvNameFile.setText(document.getNameDocument());
            } else {
                binding.ivDeleteItem.setVisibility(View.VISIBLE);
                binding.ivDocument.setVisibility(View.VISIBLE);
                if (!document.getLinkDocument().contains(".pdf") && document.getLinkDocument().contains("image")) {
                    Glide.with(context).load(document.getLinkDocument()).into(binding.ivDocument);
                }else if (document.getLinkDocument().contains(".pdf")){
                    Glide.with(context).load(R.drawable.pdf_icon).into(binding.ivDocument);
                }else {
                    Glide.with(context).load(R.drawable.word_icon).into(binding.ivDocument);
                }
                binding.tvNameFile.setText(document.getNameDocument());
                binding.ivDeleteItem.setOnClickListener(e -> {
                    iClickFileAttached.deleteFile(document);
                });
                binding.layoutDocument.setOnClickListener(e -> {
                    iClickFileAttached.getFile(document);
                });
            }
        }
    }
}

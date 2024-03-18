package com.theodoilamviec.Account.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theodoilamviec.Project;
import com.theodoilamviec.theodoilamviec.databinding.ItemProjectBinding;

import java.util.ArrayList;
import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    public interface IClickProject{
        void getProject(Project project);
    }
    private final List<Project> projectList = new ArrayList<>();
    private IClickProject iClickProject ;

    public ProjectAdapter(IClickProject iClickProject) {
        this.iClickProject = iClickProject;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<Project> data){
        projectList.clear();
        projectList.addAll(data);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProjectViewHolder(ItemProjectBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {

        Project project = projectList.get(position);
        holder.setProject(project,iClickProject);
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        ItemProjectBinding binding ;
        public ProjectViewHolder(@NonNull ItemProjectBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setProject(Project project, IClickProject iClickProject) {
            binding.tvNameProject.setText(project.getNameProject());
            binding.tvNameCreator.setText(project.getNameCreator());
            binding.layoutProject.setOnClickListener(e -> {
                iClickProject.getProject(project);
            });
        }
    }
}

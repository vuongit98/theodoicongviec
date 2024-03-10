package com.theodoilamviec.Account.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theodoilamviec.Account.Group;
import com.theodoilamviec.theodoilamviec.databinding.ItemGroupChatLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupViewHolder> {
    private final List<Group> groupList = new ArrayList<>();
    private IClickItem iClickItem;
    public interface IClickItem {
        void getItem(Group group);
    }

    public GroupListAdapter(IClickItem iClickItem){
        this.iClickItem = iClickItem;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<Group> data) {
        groupList.clear();
        groupList.addAll(data);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGroupChatLayoutBinding binding = ItemGroupChatLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new GroupViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = groupList.get(position);
        holder.setGroup(group,iClickItem);
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    class GroupViewHolder extends RecyclerView.ViewHolder{

        ItemGroupChatLayoutBinding binding ;
        public GroupViewHolder(@NonNull ItemGroupChatLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
        public void setGroup(Group group, IClickItem item){
            binding.tvNameGroup.setText(group.getNameGroup());
            binding.layoutGroup.setOnClickListener(e -> {
                item.getItem(group);
            });
        }
    }
}

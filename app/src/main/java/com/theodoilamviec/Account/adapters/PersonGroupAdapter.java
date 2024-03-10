package com.theodoilamviec.Account.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theodoilamviec.Account.User;
import com.theodoilamviec.theodoilamviec.databinding.ItemPersonBinding;

import java.util.ArrayList;
import java.util.List;

public class PersonGroupAdapter extends RecyclerView.Adapter<PersonGroupAdapter.PersonGroupViewHolder> {
    private List<User> usersList = new ArrayList<>();
    private IClickPersonItem iClickPersonItem ;
    public interface IClickPersonItem {
        void getPerson(User user);
    }
    public PersonGroupAdapter(IClickPersonItem item) {
        this.iClickPersonItem = item ;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<User> data) {
        usersList.clear();
        usersList.addAll(data);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public PersonGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPersonBinding binding = ItemPersonBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new PersonGroupViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonGroupViewHolder holder, int position) {
        User user = usersList.get(position);
        holder.setPerson(user,iClickPersonItem);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    static class PersonGroupViewHolder extends RecyclerView.ViewHolder{

        ItemPersonBinding binding ;
        public PersonGroupViewHolder(@NonNull ItemPersonBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
        public void setPerson(User user, IClickPersonItem item){
            binding.tvPerson.setText(user.getUserName());
            binding.btnAdd.setOnClickListener(e ->{
                item.getPerson(user);
            });
        }
    }
}

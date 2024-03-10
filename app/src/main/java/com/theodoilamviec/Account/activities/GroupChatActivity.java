package com.theodoilamviec.Account.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theodoilamviec.Account.Group;
import com.theodoilamviec.Account.adapters.GroupListAdapter;
import com.theodoilamviec.theodoilamviec.databinding.ActivityGroupChatBinding;
import com.theodoilamviec.theodoilamviec.databinding.ItemDialogCreateGroupBinding;

import java.util.ArrayList;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity implements GroupListAdapter.IClickItem {

    ActivityGroupChatBinding binding ;
    GroupListAdapter groupListAdapter ;
    List<Group> groupsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        groupListAdapter = new GroupListAdapter(this);
        groupListAdapter.submitList(groupsList);
        binding.rcvGroup.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        binding.rcvGroup.setAdapter(groupListAdapter);
        FirebaseDatabase.getInstance().getReference("Groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChildren()){
                    groupsList.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        Group group = dataSnapshot.getValue(Group.class);
                        System.out.println("group =  "+ group);
                        if (group != null) {
                            groupsList.add(group);
                        }
                    }
                    groupListAdapter.submitList(groupsList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GroupChatActivity.this, "Error = "+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.ftbAdd.setOnClickListener(e ->{
            showDialog();
        });
    }
    public void showDialog() {
        Dialog dialog = new Dialog(this);
        ItemDialogCreateGroupBinding binding1 = ItemDialogCreateGroupBinding.inflate(getLayoutInflater());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(binding1.getRoot());

        Window window = dialog.getWindow();
        if (window == null) return;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        binding1.btnCreate.setOnClickListener(e ->{
            String groupName = binding1.edtGroup.getText().toString().trim();

            Group group = new Group(String.valueOf(System.currentTimeMillis()),groupName, new ArrayList<>());
            FirebaseDatabase.getInstance().getReference("Groups").child(group.getIdGroup())
                    .setValue(group);
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void getItem(Group group) {
        Intent intentGroup = new Intent(this, ChatGroupActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("group", group);
        intentGroup.putExtras(bundle);
        startActivity(intentGroup);
    }
}
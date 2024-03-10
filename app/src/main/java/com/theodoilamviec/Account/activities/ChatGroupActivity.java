package com.theodoilamviec.Account.activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theodoilamviec.Account.Group;
import com.theodoilamviec.Account.Message;
import com.theodoilamviec.Account.User;
import com.theodoilamviec.Account.adapters.MessageListAdapter;
import com.theodoilamviec.Account.adapters.PersonGroupAdapter;
import com.theodoilamviec.theodoilamviec.R;
import com.theodoilamviec.theodoilamviec.databinding.ActivityChatGroupBinding;
import com.theodoilamviec.theodoilamviec.databinding.ItemDialogPersonListBinding;
import com.theodoilamviec.theodoilamviec.databinding.ItemPersonBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ChatGroupActivity extends AppCompatActivity implements PersonGroupAdapter.IClickPersonItem {

    ActivityChatGroupBinding binding;
    Group group;
    Dialog dialog;
    PersonGroupAdapter personGroupAdapter;
    List<String> uidGroupsList = new ArrayList<>();
    List<User> usersList = new ArrayList<>();
    List<Message> messageList = new ArrayList<>();
    MessageListAdapter messageListAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                group = bundle.getParcelable("group", Group.class);
            } else {
                group = bundle.getParcelable("group");
            }
        }
        binding = ActivityChatGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        messageListAdapter = new MessageListAdapter(FirebaseAuth.getInstance().getUid());
        binding.rcvChatMessage.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        binding.rcvChatMessage.setAdapter(messageListAdapter);

        if (group != null) {
            binding.tvNameGroup.setText(group.getNameGroup());
        }
        binding.ivAddGroup.setOnClickListener(e -> {
            showDialog();
        });

        binding.ivDelete.setOnClickListener(e -> {
            deleteGroup();
        });

        FirebaseDatabase.getInstance().getReference("chat_groups")
                .child(group.getIdGroup())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChildren()) {
                    messageList.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                        Message message = dataSnapshot.getValue(Message.class);
                        if (message != null) {
                            messageList.add(message);
                        }
                    }
                    messageListAdapter.submitList(messageList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("onCancelled", "Error = " + error.getMessage());

            }
        });

        FirebaseDatabase.getInstance().getReference("GroupsPerson")
                .child(group.getIdGroup())
                .addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChildren()){
                    uidGroupsList.clear();
                    int count = 0;
                    StringBuilder tempUser = new StringBuilder();
                    snapshot.getChildren().forEach(it -> System.out.println(it.getValue()));
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        User value = dataSnapshot.getValue(User.class);
                        System.out.println("user="+value);
                        uidGroupsList.add(Objects.requireNonNull(value).getUid());
                        count++;
                        if(count <= 2) {
                            if (count == 2) {
                                tempUser.append(value.getUserName().substring(0, value.getUserName().indexOf("@gmail")));
                            }else {
                                tempUser.append(value.getUserName().substring(0, value.getUserName().indexOf("@gmail"))).append(",");
                            }
                        }else {
                            tempUser.append("...");
                        }
                    }
                    binding.tvNamePerson.setText(tempUser);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("onCancelled", "Error = " + error.getMessage());
            }
        });

        binding.ivSend.setOnClickListener(e -> {
            Message message = new Message(FirebaseAuth.getInstance().getUid(),"", String.valueOf(System.currentTimeMillis()),
                    binding.edtMessage.getText().toString().trim()
                    , System.currentTimeMillis());

            sendMessage(message);
        });

    }
    public void addPeopleGroup(User user) {
        FirebaseDatabase.getInstance().getReference("GroupsPerson").child(group.getIdGroup()).push().setValue(user);
    }
    public void deleteGroup() {
        if (group.getIdGroup() != null) {
            FirebaseDatabase.getInstance().getReference("Groups").child(group.getIdGroup()).removeValue();
            FirebaseDatabase.getInstance().getReference("GroupsPerson").child(group.getIdGroup()).removeValue();
        }
    }
    public void sendMessage(Message message) {
        binding.edtMessage.setText("");
        FirebaseDatabase.getInstance().getReference("chat_groups").child(group.getIdGroup())
                .child(message.getIdMessage()).setValue(message);
    }
    public void showDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ItemDialogPersonListBinding binding1 = ItemDialogPersonListBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding1.getRoot());
        personGroupAdapter = new PersonGroupAdapter(this);

        Window window = dialog.getWindow();
        if (window == null) return;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        List<User> usersListFB = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChildren()) {
                    usersListFB.clear();
                    StringBuilder tempUser = new StringBuilder();
                    int count = 0;
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            if (user.getUid().equals(FirebaseAuth.getInstance().getUid())) {
                                continue;
                            }
                            if ( !uidGroupsList.contains(user.getUid())){
                                usersListFB.add(user);
                            }else {
                                System.out.println("name = " + user.getUserName());
                                if (count <= 1) {
                                    tempUser.append(user.getUserName().substring(0, user.getUserName().indexOf("@gmail"))).append(",");
                                }
                                count++;
                            }
                        }
                    }
                    binding.tvNamePerson.setText(tempUser);
                    if (!usersListFB.isEmpty()){
                          personGroupAdapter.submitList(usersListFB);
                        if (dialog != null) {
                            dialog.show();
                        }
                    }else {
                        Toast.makeText(ChatGroupActivity.this, "Mọi người đã được thêm hết!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d( "onCancelled: ","Error = "+ error.getMessage());
            }
        });

        binding1.rcvPerson.setAdapter(personGroupAdapter);
        binding1.rcvPerson.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        
    }
    @Override
    public void getPerson(User user) {
        addPeopleGroup(user);
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
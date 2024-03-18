package com.theodoilamviec.Account.adapters;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theodoilamviec.Account.PermissionProject;
import com.theodoilamviec.Account.User;
import com.theodoilamviec.Account.activities.MainActivity;
import com.theodoilamviec.Project;
import com.theodoilamviec.service.BackgroundNotificationService;
import com.theodoilamviec.theodoilamviec.Menu.HomeActivity;
import com.theodoilamviec.theodoilamviec.databinding.ActivityProjectBinding;
import com.theodoilamviec.theodoilamviec.databinding.ItemDialogProjectBinding;
import com.theodoilamviec.theodoilamviec.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProjectActivity extends AppCompatActivity implements ProjectAdapter.IClickProject {

    ProjectAdapter projectAdapter;
    ActivityProjectBinding binding;

    static User user;
    String uid = FirebaseAuth.getInstance().getUid();
    List<Project> projectsList = new ArrayList<>();

    private ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
                if (!isGranted.containsValue(false)) {
                    Toast.makeText(this, "Đã cấp quyền đầy đủ", Toast.LENGTH_SHORT).show();
                }else {
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Project");
        binding.toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(ProjectActivity.this, MainActivity.class)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(new String[]{
                        Manifest.permission.SCHEDULE_EXACT_ALARM,
                        Manifest.permission.USE_EXACT_ALARM,
                        Manifest.permission.POST_NOTIFICATIONS,
                        Manifest.permission.READ_MEDIA_VIDEO,
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_AUDIO,

                });
            }
        }

        projectAdapter = new ProjectAdapter(this);
        binding.rcvProject.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rcvProject.addItemDecoration(new GridSpacingItemDecoration(2, 10, true));
        binding.rcvProject.setAdapter(projectAdapter);

        binding.fbtAdd.setOnClickListener(e -> {
            showDialogProject();
        });

        FirebaseDatabase.getInstance().getReference("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    user = snapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("Projects")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists() && snapshot.hasChildren()) {

                            projectsList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                Project project = dataSnapshot.getValue(Project.class);
                                if (project != null) {
                                    projectsList.add(project);
                                }
                            }
                            binding.progressBar.setVisibility(View.GONE);

                            Intent intentSerivce = new Intent(ProjectActivity.this, BackgroundNotificationService.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("project", new ArrayList<>(projectsList));
                            intentSerivce.putExtras(bundle);
                            startService(intentSerivce);

                            projectAdapter.submitList(projectsList);
                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public void getProject(Project project) {
        Intent intentProject = new Intent(ProjectActivity.this, HomeActivity.class);
        intentProject.putExtra("id_project", project.getIdProject());

        FirebaseDatabase.getInstance()
                .getReference("PermissionProject")
                .child(uid)
                .child(project.getIdProject())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            startActivity(intentProject);
                        }
                        else{
                            Toast.makeText(ProjectActivity.this, "Bạn không có quyền vào dự án!. Vui lòng liên" +
                                    "hệ người tạo dự án", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    void showDialogProject() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ItemDialogProjectBinding binding = ItemDialogProjectBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        Window window = dialog.getWindow();
        if (window == null) return;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        binding.btnCancel.setOnClickListener(e -> {
            dialog.dismiss();
        });

        binding.btnOk.setOnClickListener(e -> {
            String idProject = String.valueOf(System.currentTimeMillis());
            String userName = user.getUserName();
            Project project = new Project(idProject, binding.edtNameProject.getText().toString().trim()
                    , userName.substring(0, userName.indexOf("@gmail")));

            PermissionProject permissionProject = new PermissionProject(
                    String.valueOf(System.currentTimeMillis()),
                    user.getUid(),
                    project.getIdProject()
            );
            setProject(project);
            setProjectPermission(permissionProject);

            dialog.dismiss();
        });

        dialog.show();
    }

    void setProject(Project project) {
        FirebaseDatabase.getInstance().getReference("Projects")
                .child(project.getIdProject())
                .setValue(project);
    }

    void setProjectPermission(PermissionProject projectPermission) {

        FirebaseDatabase.getInstance().getReference("PermissionProject")
                .child(uid)
                .child(projectPermission.getIdProject())
                .setValue(projectPermission);

    }

}
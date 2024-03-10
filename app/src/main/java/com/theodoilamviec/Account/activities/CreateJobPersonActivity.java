package com.theodoilamviec.Account.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theodoilamviec.Account.Job;
import com.theodoilamviec.Account.JobDocument;
import com.theodoilamviec.Account.JobUser;
import com.theodoilamviec.Account.User;
import com.theodoilamviec.Account.adapters.FileAttachedAdapter;
import com.theodoilamviec.Account.adapters.PersonGroupAdapter;
import com.theodoilamviec.theodoilamviec.databinding.ActivityCreateJobPersonBinding;
import com.theodoilamviec.theodoilamviec.databinding.ItemDialogContentFileBinding;
import com.theodoilamviec.theodoilamviec.databinding.ItemDialogPersonListBinding;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CreateJobPersonActivity extends AppCompatActivity implements PersonGroupAdapter.IClickPersonItem {


    ActivityCreateJobPersonBinding binding;
    Long startTime = 0L;
    Long endTime = 0L;
    int highPriority = 0;
    Dialog dialog;
    Dialog dialogFile;
    List<User> userChooseList = new ArrayList<>();
    PersonGroupAdapter personGroupAdapter;
    String[] priorityList = new String[]{"New", "Medium", "Urgent"};
    List<User> usersList = new ArrayList<>();
    List<String> usersInGroupList = new ArrayList<>();

    FileAttachedAdapter fileAttachedAdapter;
    List<Uri> uriImageList = new ArrayList<>();
    List<Uri> uriPdfList = new ArrayList<>();
    List<Uri> uriWordList = new ArrayList<>();

    String idJob;

    List<JobDocument> jobDocumentList = new ArrayList<>();

    StorageReference storageReferenceImages;
    StorageReference storageReferencePdf;
    StorageReference storageReferenceWord;

    private final ActivityResultLauncher<Intent> pdfPickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri pdfUri = data.getData();
                        String fileName;
                        if (pdfUri != null) {
                            fileName = getFileName(pdfUri);
                            StorageReference pdfStorage = storageReferencePdf.child(fileName);
                            pdfStorage.putFile(pdfUri).addOnSuccessListener(taskSnapshot -> {
                                pdfStorage.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String idJobDocument = String.valueOf(System.currentTimeMillis() + 1);
                                    JobDocument jobDocument = new JobDocument(idJob, idJobDocument, fileName, uri.toString());
                                    jobDocumentList.add(jobDocument);
                                    fileAttachedAdapter.submitList(jobDocumentList);
                                });
                            });
                        } else {
                            fileName = null;
                        }
                        if (dialogFile != null) dialogFile.dismiss();
                    }
                } else {
                    Toast.makeText(CreateJobPersonActivity.this, "No PDF selected", Toast.LENGTH_SHORT).show();
                }
            });

    private String getFileName(Uri uri) {
        String result = null;
        if (Objects.equals(uri.getScheme(), "content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (columnIndex != -1) {
                        result = cursor.getString(columnIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }
    private final ActivityResultLauncher<Intent> wordPickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri wordUri = data.getData();
                        String fileName;
                        if (wordUri != null) {
                            fileName = getFileName(wordUri);
                            StorageReference wordStorage = storageReferenceWord.child(fileName);
                            wordStorage.putFile(wordUri).addOnSuccessListener(taskSnapshot -> {
                                wordStorage.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String idJobDocument = String.valueOf(System.currentTimeMillis() + 2);
                                    JobDocument jobDocument = new JobDocument(idJob, idJobDocument, fileName, uri.toString());
                                    jobDocumentList.add(jobDocument);
                                    fileAttachedAdapter.submitList(jobDocumentList);
                                });
                            });
                        } else {
                            fileName = null;
                        }
                        if (dialogFile != null) dialogFile.dismiss();
                    }
                } else {
                    Toast.makeText(CreateJobPersonActivity.this, "No PDF selected", Toast.LENGTH_SHORT).show();
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateJobPersonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        idJob = String.valueOf(System.currentTimeMillis());
        fileAttachedAdapter = new FileAttachedAdapter();

        storageReferenceImages = FirebaseStorage.getInstance().getReference("images");
        storageReferencePdf = FirebaseStorage.getInstance().getReference("pdfs");
        storageReferenceWord = FirebaseStorage.getInstance().getReference("words");

        binding.rcvFile.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.rcvFile.setAdapter(fileAttachedAdapter);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, priorityList);
        binding.spPriority.setAdapter(adapter);

        getUsersInGroupList();
        binding.spPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                highPriority = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.btnAdd.setOnClickListener(e -> {
            String nameJob = binding.edtNameJob.getText().toString().trim();
            Job job = new Job(idJob, nameJob, startTime, endTime, highPriority);
            setJob(job);

            for (User user : userChooseList) {
                String idJobUser = String.valueOf(System.currentTimeMillis() + 2);
                JobUser jobUser = new JobUser(idJob, idJobUser, user);
                setJobUser(jobUser);
            }
        });
        binding.layoutStartDate.setOnClickListener(e -> {
            showDialogTime(true);
        });
        binding.layoutEndDate.setOnClickListener(e -> {
            showDialogTime(false);
        });

        binding.ivAttachFile.setOnClickListener(e -> {
            showDialogAttachFile();
        });

        binding.ivAttachPeople.setOnClickListener(e -> {
            showDialog();
            getListPerson();

        });

//        getListFile();
    }

    public void getUsersInGroupList() {
        FirebaseDatabase.getInstance().getReference("JobUsers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists() && snapshot.hasChildren()) {
                            List<String> nameUser = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                JobUser jobUser = dataSnapshot.getValue(JobUser.class);
                                if (jobUser != null) {
                                    usersInGroupList.add(jobUser.getUser().getUid());
                                    String name = jobUser.getUser().getUserName();
                                    nameUser.add(name.substring(0, name.indexOf("@gmail")));
                                }
                            }
                            String nameList = String.join(", ", nameUser);
                            binding.tvNamePerson.setText(nameList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("onCancelled: ", "Error = " + error.getMessage());
                    }
                });
    }

    public void getListPerson() {
        FirebaseDatabase.getInstance().getReference("Users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.hasChildren()) {
                            usersList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                User user = dataSnapshot.getValue(User.class);

                                if (Objects.requireNonNull(user).getUid().equals(FirebaseAuth.getInstance().getUid())) {
                                    continue;
                                }
                                if (usersInGroupList.contains(user.getUid())) {
                                    continue;
                                }

                                usersList.add(user);

                            }
                            if (!usersList.isEmpty()) {
                                personGroupAdapter.submitList(usersList);
                                if (dialog != null) {
                                    dialog.show();
                                }
                            } else {
                                Toast.makeText(CreateJobPersonActivity.this, "Mọi người đã được thêm hết!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void showDialogTime(Boolean isStarted) {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, i, i1, i2) -> {
            Calendar getTime = Calendar.getInstance();
            getTime.set(i, i1, i2);
            if (isStarted) {
                startTime = getTime.getTimeInMillis();
                binding.tvStartDatePicker.setText(i2 + "/" + i1 + "/" + i);
            } else {
                endTime = getTime.getTimeInMillis();
                binding.tvEndDatePicker.setText(i2 + "/" + i1 + "/" + i);

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void setJob(Job job) {
        FirebaseDatabase.getInstance().getReference("Jobs").child(job.getIdJob())
                .setValue(job);
    }

    public void setJobDocument(JobDocument jobDocument) {
        FirebaseDatabase.getInstance().getReference("JobDocuments").child(jobDocument.getIdJob())
                .setValue(jobDocument);
    }

    public void setJobUser(JobUser jobUser) {
        FirebaseDatabase.getInstance().getReference("JobUsers").child(jobUser.getIdJob())
                .setValue(jobUser);
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

        binding1.rcvPerson.setAdapter(personGroupAdapter);
        binding1.rcvPerson.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    }

    public void getListFile() {
        FirebaseDatabase.getInstance().getReference("JobDocuments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists() && snapshot.hasChildren()) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                JobDocument jobDocument = dataSnapshot.getValue(JobDocument.class);
                                if (jobDocument != null) {
                                    jobDocumentList.add(jobDocument);
                                }
                            }
                            fileAttachedAdapter.submitList(jobDocumentList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("onCancelled: ", "Error = " + error.getMessage());
                    }
                });
    }

    public void showDialogAttachFile() {
        dialogFile = new Dialog(this);
        dialogFile.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ItemDialogContentFileBinding binding1 = ItemDialogContentFileBinding.inflate(getLayoutInflater());
        dialogFile.setContentView(binding1.getRoot());
        personGroupAdapter = new PersonGroupAdapter(this);

        Window window = dialogFile.getWindow();
        if (window == null) return;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        binding1.btnImages.setOnClickListener(e -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(new String[]{
                        Manifest.permission.READ_MEDIA_IMAGES,
                });
            } else {
                requestPermissionLauncher.launch(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                });
            }
        });
        binding1.btnPdf.setOnClickListener(e -> {
            openPdfPicker();
        });
        binding1.btnWord.setOnClickListener(e -> {
            openWordsPicker();
        });
        dialogFile.show();
    }
    private void openPdfPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        pdfPickerLauncher.launch(intent);
    }
    private void openWordsPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/msword|application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        wordPickerLauncher.launch(intent);
    }

    private ActivityResultLauncher<PickVisualMediaRequest> pickImage =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    uriImageList.add(uri);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
                    Date dateNow = new Date();
                    String fileName = dateFormat.format(dateNow);
                    StorageReference imageStorage = storageReferenceImages.child(fileName);
                    imageStorage.putFile(uri)
                            .addOnSuccessListener(taskSnapshot -> {
                                imageStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String idJobDocument = String.valueOf(System.currentTimeMillis() + 1);
                                        JobDocument jobDocument = new JobDocument(idJob, idJobDocument, fileName, uri.toString());
                                        jobDocumentList.add(jobDocument);
                                        fileAttachedAdapter.submitList(jobDocumentList);
                                    }
                                });
                            });
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });


    private ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
                if (!isGranted.containsValue(false)) {
                    pickImage.launch(new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build());
                }
            });

    @Override
    public void getPerson(User user) {
        userChooseList.add(user);
    }
}
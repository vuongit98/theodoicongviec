package com.theodoilamviec.Account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.theodoilamviec.theodoilamviec.R;

public class QuenMatKhau extends AppCompatActivity  implements UserView ,
        View.OnClickListener {
   
    private Toolbar toolbar;
    private Button btnxacnhan;
    private EditText editEmail;
 
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        InitWidget();
        Init();
        HandleEVents();
    }

    private void HandleEVents() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       
    }

    private void Init() {
        btnxacnhan.setOnClickListener(this);
        
    }

    private void InitWidget() {
        btnxacnhan =findViewById(R.id.btnxacnhan);
        toolbar = findViewById(R.id.toolbar);
        editEmail = findViewById(R.id.editEmail);
    }

    @Override
    public void OnSucess() {
        startActivity(new Intent(QuenMatKhau.this, DangNhapActivity.class));
        finish();
    }

    @Override
    public void OnFail() {

    }

    @Override
    public void OnValidEmail() {
        Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnEmptyEmail() {
        Toast.makeText(this, "Email không để trống ", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void Onpass() {
        Toast.makeText(this, "Mật khẩu không để trống ", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void OnpassSame() {

    }

    @Override
    public void OnAuthEmail() {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
        Toast.makeText(this, "Làm ơn vào Email để xác thực !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnEmpty() {

    }

    @Override
    public void getProfileUser(int iduser,String hoten, String avatar, double chieucao, double cannang, String gioitinh, String date_of_birth) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnxacnhan) {
            String email = editEmail.getText().toString().trim();
            if (email.length() > 0) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(QuenMatKhau.this, "Vui lòng vào Email ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(QuenMatKhau.this, "Email không tồn tại / Mất kết nối internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else {
                Toast.makeText(this, "Vui lòng nhập email  !", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

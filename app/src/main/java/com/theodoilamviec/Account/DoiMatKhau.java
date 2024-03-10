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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.theodoilamviec.theodoilamviec.R;

public class DoiMatKhau extends AppCompatActivity  implements UserView ,
        View.OnClickListener {
   
    private Toolbar toolbar;
    private Button btnxacnhan;
    private EditText editPass,editRepass,editPassOld;
 
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);
        InitWidget();
        Init();
        HandleEVents();
    }

    private void HandleEVents() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Đổi mật khẩu");
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
        editPass = findViewById(R.id.editPass);
        editRepass = findViewById(R.id.editRePass);
        editPassOld = findViewById(R.id.editPassOld);
    }

    @Override
    public void OnSucess() {
        startActivity(new Intent(DoiMatKhau.this, DangNhapActivity.class));
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
            String pass = editPass.getText().toString().trim();
            String repasss = editRepass.getText().toString().trim();
            String pass_old = editPassOld.getText().toString().trim();
            if (pass.length() >= 6) {
                if (pass.equalsIgnoreCase(repasss)) {
                    FirebaseAuth.getInstance().
                            signInWithEmailAndPassword(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                    pass_old).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseAuth.getInstance().getCurrentUser().updatePassword(pass)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            finish();
                                                            Toast.makeText(DoiMatKhau.this, "Đổi mật khẩu thành công !", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                });
                                    } else {
                                        Toast.makeText(DoiMatKhau.this, "Mật khẩu cũ không chính xác", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                } else {
                    Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(this, "Mật khẩu tối thiểu 6 ký tự", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

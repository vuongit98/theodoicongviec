package com.theodoilamviec.Account;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.theodoilamviec.theodoilamviec.R;

import java.util.Objects;

// ban nay la ban coppy phai k
public class DangKy extends AppCompatActivity  implements UserView ,
        View.OnClickListener {
    private UserController userController;
    private EditText editEmail,editPass,editRepass;
    private Button btndangnhap;
    private ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        InitWidget();
        Init();
        HandleEVents();
    }

    private void HandleEVents() {
        btndangnhap.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    private void Init() {
        userController = new UserController(this,this);
    }

    private void InitWidget() {
        editEmail = findViewById(R.id.editEmail);
        editPass= findViewById(R.id.editPass);
        editRepass= findViewById(R.id.editRePass);
        btndangnhap = findViewById(R.id.btndangnhap);
        back = findViewById(R.id.back);


    }

    @Override
    public void OnSucess() {
        Toast.makeText(this, "Làm ơn vào email xác thực tài khoản !", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void OnFail() {
        Toast.makeText(this, "Đăng ký thất bại ", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "Mật khẩu không khớp ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnAuthEmail() {
        Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).sendEmailVerification();
        Toast.makeText(this, "Làm ơn vào Email để xác thực !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnEmpty() {

    }

    @Override
    public void getProfileUser(int iduser,String hoten, String avatar, double chieucao, double cannang, String gioitinh, String date_of_birth) {

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btndangnhap) {
            String email = editEmail.getText().toString().trim();
            String pass = editPass.getText().toString().trim();
            String repass = editRepass.getText().toString().trim();
            userController.HandleRegistUser(email,pass,repass);
        }else {
            finish();
        }
    }
}

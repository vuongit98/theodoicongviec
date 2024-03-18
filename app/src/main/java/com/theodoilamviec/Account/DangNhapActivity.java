package com.theodoilamviec.Account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.google.firebase.auth.FirebaseAuth;
import com.theodoilamviec.Account.adapters.ProjectActivity;
import com.theodoilamviec.theodoilamviec.Menu.HomeActivity;
import com.theodoilamviec.theodoilamviec.R;

import com.theodoilamviec.theodoilamviec.databinding.ActivitySignInBinding;

public class DangNhapActivity extends AppCompatActivity implements UserView,
        View.OnClickListener {
    private UserController userController;

    private Toolbar toolbar;

    ActivitySignInBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userController = new UserController(this, this);
        binding.btndangnhap.setOnClickListener(this);
        binding.txtforget.setOnClickListener(this);
        binding.txtsignUp.setOnClickListener(this);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            startActivity(new Intent(DangNhapActivity.this, ProjectActivity.class));
        }
    }


    @Override
    public void OnSucess() {
        startActivity(new Intent(DangNhapActivity.this, ProjectActivity.class));

    }

    @Override
    public void OnFail() {
        Toast.makeText(this, "Sai email / mat khau ", Toast.LENGTH_SHORT).show();

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
    public void getProfileUser(int iduser, String hoten, String avatar, double chieucao, double cannang, String gioitinh, String date_of_birth) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btndangnhap) {
            String email = binding.editEmail.getText().toString().trim();
            String pass = binding.editPass.getText().toString().trim();
            userController.HandleLoginUser(email, pass);
        } else if (v.getId() == R.id.txtforget) {
            startActivity(new Intent(DangNhapActivity.this, QuenMatKhau.class));
        } else if (v.getId() == R.id.txtsignUp) {
            startActivity(new Intent(DangNhapActivity.this, DangKy.class));
        }
    }
}

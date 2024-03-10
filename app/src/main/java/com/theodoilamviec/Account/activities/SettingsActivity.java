package com.theodoilamviec.Account.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.theodoilamviec.Account.DangNhapActivity;
import com.theodoilamviec.Account.DoiMatKhau;
import com.theodoilamviec.theodoilamviec.R;

import com.theodoilamviec.theodoilamviec.databinding.ActivitySettingsBinding;
import com.theodoilamviec.Account.sheets.PinOptionsBottomSheetModal;

public class SettingsActivity extends AppCompatActivity implements PinOptionsBottomSheetModal.OnRemoveListener {


    ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.goBack.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, MainActivity.class));

            finish();
        });


        binding.signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SettingsActivity.this, DangNhapActivity.class));
            }
        });

        binding.aboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, DoiMatKhau.class));
            }
        });


        binding.pinLock.setOnClickListener(v -> pinLock());

    }


    private void pinLock() {
        PinOptionsBottomSheetModal pinOptionsBottomSheetModal = new PinOptionsBottomSheetModal();
        pinOptionsBottomSheetModal.show(getSupportFragmentManager(), "TAG");
    }

    @Override
    public void onRemoveListener(int requestCode) {
        if (requestCode == PinOptionsBottomSheetModal.REQUEST_REMOVE_PIN_CODE) {
            Toast.makeText(this, getString(R.string.pin_code_unset), Toast.LENGTH_SHORT).show();
        }
    }

}
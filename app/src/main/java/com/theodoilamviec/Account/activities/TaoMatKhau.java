package com.theodoilamviec.Account.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.theodoilamviec.theodoilamviec.R;

import com.theodoilamviec.theodoilamviec.databinding.ActivityCreatePinBinding;
import com.theodoilamviec.theodoilamviec.sharedPreferences.SharedPref;

public class TaoMatKhau extends AppCompatActivity {

    private SharedPref sharedPref;
    private EditText pinCode;
    private Button pinCodeAction;
    private TextView length;
    ActivityCreatePinBinding activityCreatePinBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        super.onCreate(savedInstanceState);
        activityCreatePinBinding = ActivityCreatePinBinding.inflate(getLayoutInflater());


        setContentView(activityCreatePinBinding.getRoot());

        // return back and finish activity

        activityCreatePinBinding.goBack.setOnClickListener(v -> finish());

        // pin code

        activityCreatePinBinding.pinCode.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        activityCreatePinBinding.pinCode.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
        activityCreatePinBinding.pinCode.addTextChangedListener(pinCodeTextWatcher);

        // length
        length = findViewById(R.id.length);

        // pin code is not set
        // set a new pin code
        activityCreatePinBinding.pinCodeAction.setText(getString(R.string.save));
        activityCreatePinBinding.pinCodeAction.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(activityCreatePinBinding.pinCode.getText().toString())) {
                sharedPref.setNotePinCode(Integer.parseInt(activityCreatePinBinding.pinCode.getText().toString()));
                Toast.makeText(this, getString(R.string.pin_code_set), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("result", "lock");
                setResult(Activity.RESULT_OK, intent);
            } else {
                Toast.makeText(this, getString(R.string.pin_code_empty), Toast.LENGTH_SHORT).show();
            }
            finish();
        });

    }

    /**
     * text watcher enables action
     * button when pin code field not empty
     */
    private final TextWatcher pinCodeTextWatcher = new TextWatcher() {
        @SuppressLint("SetTextI18n")
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            length.setText(s.length() + "/8");
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Set length while text changing
            length.setText(s.length() + "/8");

            activityCreatePinBinding.pinCodeAction.setEnabled(s.length() == 8);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(TaoMatKhau.this, MainActivity.class));
    }
}
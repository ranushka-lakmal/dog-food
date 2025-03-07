package com.example.dogfoodapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dogfoodapp.database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etLocation;
    private RadioGroup rgUserType, rgPaymentType;
    private LinearLayout paymentTypeContainer;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        setupUserTypeListener();
        setupRegisterButton();
    }

    private void initializeViews() {
        etEmail = findViewById(R.id.etRegEmail);
        etPassword = findViewById(R.id.etRegPassword);
        etLocation = findViewById(R.id.etLocation);
        rgUserType = findViewById(R.id.rgUserType);
        rgPaymentType = findViewById(R.id.rgPaymentType);
        paymentTypeContainer = findViewById(R.id.paymentTypeContainer);
    }

    private void setupUserTypeListener() {
        rgUserType.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = findViewById(checkedId);
            String userType = selectedRadioButton.getTag().toString();

            if (userType.equals("User")) {
                paymentTypeContainer.setVisibility(View.VISIBLE);
            } else {
                paymentTypeContainer.setVisibility(View.GONE);
                rgPaymentType.clearCheck();
            }
        });
    }

    private void setupRegisterButton() {
        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> attemptRegistration());
    }

    private void attemptRegistration() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        int selectedUserTypeId = rgUserType.getCheckedRadioButtonId();
        int selectedPaymentTypeId = rgPaymentType.getCheckedRadioButtonId();

        if (!validateInputs(email, password, location, selectedUserTypeId, selectedPaymentTypeId)) {
            return;
        }

        RadioButton userTypeRadio = findViewById(selectedUserTypeId);
        String userType = userTypeRadio.getTag().toString();
        String paymentType = "";

        if (userType.equals("User")) {
            RadioButton paymentTypeRadio = findViewById(selectedPaymentTypeId);
            paymentType = paymentTypeRadio.getText().toString();
        }

        registerUser(email, password, userType, location, paymentType);
    }

    private boolean validateInputs(String email, String password, String location,
                                   int userTypeId, int paymentTypeId) {
        if (email.isEmpty() || password.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (userTypeId == -1) {
            Toast.makeText(this, "Please select user type", Toast.LENGTH_SHORT).show();
            return false;
        }

        RadioButton userTypeRadio = findViewById(userTypeId);
        String userType = userTypeRadio.getTag().toString();

        if (userType.equals("User") && paymentTypeId == -1) {
            Toast.makeText(this, "Please select payment type", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void registerUser(String email, String password, String userType,
                              String location, String paymentType) {
        if (dbHelper.addUser(email, password, userType, location, paymentType)) {
            saveUserData(email, userType);
            navigateToMainActivity();
        } else {
            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserData(String email, String userType) {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        prefs.edit()
                .putString("USER_EMAIL", email)
                .putString("USER_TYPE", userType)
                .apply();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
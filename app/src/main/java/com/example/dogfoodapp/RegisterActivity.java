package com.example.dogfoodapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dogfoodapp.database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etLocation;
    private RadioGroup rgUserType, rgPaymentType;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);
        etEmail = findViewById(R.id.etRegEmail);
        etPassword = findViewById(R.id.etRegPassword);
        etLocation = findViewById(R.id.etLocation);
        rgUserType = findViewById(R.id.rgUserType);
        rgPaymentType = findViewById(R.id.rgPaymentType);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String location = etLocation.getText().toString().trim();

            int selectedUserTypeId = rgUserType.getCheckedRadioButtonId();
            int selectedPaymentTypeId = rgPaymentType.getCheckedRadioButtonId();

            if (email.isEmpty() || password.isEmpty() || location.isEmpty() ||
                    selectedUserTypeId == -1 || selectedPaymentTypeId == -1) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton userTypeRadio = findViewById(selectedUserTypeId);
            RadioButton paymentTypeRadio = findViewById(selectedPaymentTypeId);

            String userType = userTypeRadio.getText().toString();
            String paymentType = paymentTypeRadio.getText().toString();

            if (dbHelper.addUser(email, password, userType, location, paymentType)) {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
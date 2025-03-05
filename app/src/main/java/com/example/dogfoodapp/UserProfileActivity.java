package com.example.dogfoodapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.dogfoodapp.database.DatabaseHelper;

public class UserProfileActivity extends AppCompatActivity {

    private TextView tvEmail, tvPassword, tvLocation, tvPaymentType;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize views
        tvEmail = findViewById(R.id.tvEmail);
        tvPassword = findViewById(R.id.tvPassword);
        tvLocation = findViewById(R.id.tvLocation);
        tvPaymentType = findViewById(R.id.tvPaymentType);
        dbHelper = new DatabaseHelper(this);

        // Get email from intent
        String email = getIntent().getStringExtra("USER_EMAIL");

        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "User email not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch user details from database
        String[] userDetails = dbHelper.getUserDetails(email);

        if (userDetails != null) {
            // userDetails[] order: [0]=email, [1]=password, [2]=location, [3]=paymentType
            tvEmail.setText(userDetails[0]);
            tvPassword.setText(userDetails[1]);
            tvLocation.setText(userDetails[2]);
            tvPaymentType.setText(userDetails[3]);
        } else {
            Toast.makeText(this, "Failed to fetch user details!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
package com.example.dogfoodapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dogfoodapp.database.DatabaseHelper;

public class UserProfileActivity extends AppCompatActivity {

    private TextView tvEmail, tvPassword;
    private EditText etLocation;
    private Spinner spinnerPaymentType;
    private DatabaseHelper dbHelper;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize views
        tvEmail = findViewById(R.id.tvEmail);
        tvPassword = findViewById(R.id.tvPassword);
        etLocation = findViewById(R.id.etLocation);
        spinnerPaymentType = findViewById(R.id.spinnerPaymentType);
        dbHelper = new DatabaseHelper(this);

        // Setup payment type spinner
        setupPaymentTypeSpinner();

        // Get email from intent
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "User email not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch and display user details
        loadUserDetails();

        // Add save button functionality
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> saveProfileChanges());
    }

    private void setupPaymentTypeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payment_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaymentType.setAdapter(adapter);
    }

    private void loadUserDetails() {
        String[] userDetails = dbHelper.getUserDetails(userEmail);

        if (userDetails != null) {
            tvEmail.setText(userDetails[0]);
            tvPassword.setText(userDetails[1]);
            etLocation.setText(userDetails[2]);

            // Set selected payment type
            String paymentType = userDetails[3];
            ArrayAdapter adapter = (ArrayAdapter) spinnerPaymentType.getAdapter();
            int position = adapter.getPosition(paymentType);
            spinnerPaymentType.setSelection(position);
        } else {
            Toast.makeText(this, "Failed to fetch user details!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void saveProfileChanges() {
        String newLocation = etLocation.getText().toString().trim();
        String newPaymentType = spinnerPaymentType.getSelectedItem().toString();

        if (newLocation.isEmpty()) {
            etLocation.setError("Location cannot be empty");
            return;
        }

        // Show confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_changes_title)
                .setMessage(R.string.confirm_changes_message)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    boolean success = dbHelper.updateUserProfile(userEmail, newLocation, newPaymentType);

                    if (success) {
                        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                        navigateToWelcomeScreen();
                    } else {
                        Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void navigateToWelcomeScreen() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
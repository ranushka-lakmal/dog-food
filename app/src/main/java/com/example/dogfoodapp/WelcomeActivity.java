package com.example.dogfoodapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button btnBuyProduct = findViewById(R.id.btnBuyProduct);
        btnBuyProduct.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, BuyProductActivity.class);
            startActivity(intent);
        });

//        Button btnOrderCart = findViewById(R.id.btnViewCart);
//        btnOrderCart.setOnClickListener(v -> {
//        });

        Button btnOrderCart = findViewById(R.id.btnOrderCart);
        btnOrderCart.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, ViewCartActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnEducationalContent).setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, ViewEducationalActivity.class));
        });

        ImageButton btnProfile = findViewById(R.id.btnUserProfile); // Ensure this ID matches your XML
        btnProfile.setOnClickListener(v -> {
            // Retrieve email from SharedPreferences
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String email = prefs.getString("USER_EMAIL", null);

            if (email != null) {
                Intent intent = new Intent(WelcomeActivity.this, UserProfileActivity.class);
                intent.putExtra("USER_EMAIL", email);
                startActivity(intent);
            } else {
                Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
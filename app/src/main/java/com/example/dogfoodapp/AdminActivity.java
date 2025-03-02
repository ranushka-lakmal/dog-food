package com.example.dogfoodapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button btnCategory = findViewById(R.id.btnCategory);
        Button btnProduct = findViewById(R.id.btnProduct);

        btnCategory.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, CategoryActivity.class));
        });

        btnProduct.setOnClickListener(v -> {
            // Start Product Management Activity
            startActivity(new Intent(this, ProductActivity.class));
        });
    }
}
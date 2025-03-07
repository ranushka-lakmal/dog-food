package com.example.dogfoodapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button btnCategory = findViewById(R.id.btnCategory);
        Button btnProduct = findViewById(R.id.btnProduct);
        MaterialButton btnManageCategories = findViewById(R.id.btnManageCategories);
       // Button btnManageCategories = findViewById(R.id.btnManageCategories);

        btnCategory.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, CategoryActivity.class));
        });

        btnProduct.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, AddProductActivity.class));
        });

        btnManageCategories.setOnClickListener(v -> {
            // Navigate to ManageCategoryActivity
            startActivity(new Intent(AdminActivity.this, ManageCategoryActivity.class));
        });

        findViewById(R.id.btnManageProduct).setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, ManageProductActivity.class));
        });

        findViewById(R.id.btnManageEducation).setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, EducationalContentActivity.class));
        });

        findViewById(R.id.btnManageEducation).setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, ManageEducationalContentActivity.class));
        });

//        btnManageCategories.setOnClickListener(v -> {
//            startActivity(new Intent(AdminActivity.this, ManageCategoryActivity.class));
//        });
    }
}
package com.example.dogfoodapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dogfoodapp.database.DatabaseHelper;

public class CategoryActivity extends AppCompatActivity {
    private EditText etCatId, etCatName;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        dbHelper = new DatabaseHelper(this);
        etCatId = findViewById(R.id.etCatId);
        etCatName = findViewById(R.id.etCatName);
        Button btnAddCategory = findViewById(R.id.btnAddCategory);

        btnAddCategory.setOnClickListener(v -> addCategoryToDB());
    }

    private void addCategoryToDB() {
        String catId = etCatId.getText().toString().trim();
        String catName = etCatName.getText().toString().trim();

        if (catId.isEmpty() || catName.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (dbHelper.addCategory(catId, catName)) {
                Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show();
                etCatId.setText("");
                etCatName.setText("");
            } else {
                Toast.makeText(this, "Failed to add category (DB error)", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("CATEGORY_ERROR", "Insert failed: ", e);
        }
    }
}
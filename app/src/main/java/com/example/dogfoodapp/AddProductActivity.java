package com.example.dogfoodapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dogfoodapp.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {
    private Spinner spinnerCategory;
    private EditText etProductId, etProductName, etPrice, etQuantity;
    private DatabaseHelper dbHelper;
    private List<String> categoryIds = new ArrayList<>();
    private List<String> categoryNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        loadCategories();
        setupButton();
    }

    private void initializeViews() {
        spinnerCategory = findViewById(R.id.spinnerCategory);
        etProductId = findViewById(R.id.etProductId);
        etProductName = findViewById(R.id.etProductName);
        etPrice = findViewById(R.id.etPrice);
        etQuantity = findViewById(R.id.etQuantity);
    }

    private void loadCategories() {
        Cursor cursor = dbHelper.getAllCategories();
        if (cursor.moveToFirst()) {
            do {
                categoryIds.add(cursor.getString(0));
                categoryNames.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categoryNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void setupButton() {
        Button btnAdd = findViewById(R.id.btnAddProduct);
        btnAdd.setOnClickListener(v -> addProduct());
    }

    private void addProduct() {
        String productId = etProductId.getText().toString().trim();
        String productName = etProductName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();
        int selectedPosition = spinnerCategory.getSelectedItemPosition();

        if (validateInput(productId, productName, priceStr, quantityStr, selectedPosition)) {
            String categoryId = categoryIds.get(selectedPosition);
            double price = Double.parseDouble(priceStr);
            int quantity = Integer.parseInt(quantityStr);

            if (dbHelper.addProduct(productId, productName, categoryId, price, quantity)) {
                Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInput(String productId, String productName,
                                  String price, String quantity, int selectedPosition) {
        if (productId.isEmpty() || productName.isEmpty() || price.isEmpty() || quantity.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (selectedPosition < 0) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void clearFields() {
        etProductId.setText("");
        etProductName.setText("");
        etPrice.setText("");
        etQuantity.setText("");
        spinnerCategory.setSelection(0);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
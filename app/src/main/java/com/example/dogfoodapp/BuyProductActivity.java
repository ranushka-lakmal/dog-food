package com.example.dogfoodapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogfoodapp.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class BuyProductActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private ProductAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_product);

        dbHelper = new DatabaseHelper(this);
        rvProducts = findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));

        loadProducts();
    }

    private void loadProducts() {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllProducts();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String productId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_PRODUCT_ID));
                    String productName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_PRODUCT_NAME));
                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_PRICE));
                    int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_QUANTITY));

                    productList.add(new Product(productId, productName, price, quantity));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        if (productList.isEmpty()) {
            Toast.makeText(this, "No products available", Toast.LENGTH_SHORT).show();
        }

        adapter = new ProductAdapter(productList);
        rvProducts.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
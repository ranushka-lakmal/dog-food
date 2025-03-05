package com.example.dogfoodapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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

        Button btnOrderCart = findViewById(R.id.btnViewCart);
        btnOrderCart.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, ViewCartActivity.class);
            startActivity(intent);
        });
    }
}
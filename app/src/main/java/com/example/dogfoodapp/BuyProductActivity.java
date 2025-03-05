package com.example.dogfoodapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogfoodapp.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class BuyProductActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private BuyProductAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_product);

        dbHelper = new DatabaseHelper(this);
        rvProducts = findViewById(R.id.rvProducts);

        // Set up GridLayoutManager with 2 columns
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));

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
                    String category = ""; // Default value (if category is not available)
                    if (cursor.getColumnIndex(DatabaseHelper.KEY_CATEGORY_ID) != -1) {
                        category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CATEGORY_ID));
                    }
                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_PRICE));
                    int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_QUANTITY));

                    productList.add(new Product(productId, productName, category, price, quantity));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        if (productList.isEmpty()) {
            Toast.makeText(this, "No products available", Toast.LENGTH_SHORT).show();
        }

        adapter = new BuyProductAdapter(productList);
        rvProducts.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    // Adapter for Buy Product page
    private static class BuyProductAdapter extends RecyclerView.Adapter<BuyProductAdapter.ViewHolder> {
        private final List<Product> productList;

        public BuyProductAdapter(List<Product> productList) {
            this.productList = productList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_product_buy, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Product product = productList.get(position);
            holder.tvProductName.setText(product.getName());
            holder.tvCategory.setText(product.getCategory());

            // Initialize quantity to 0
            holder.tvQuantity.setText("0");

            // Handle quantity changes
            holder.btnIncrease.setOnClickListener(v -> {
                int quantity = Integer.parseInt(holder.tvQuantity.getText().toString());
                quantity++;
                holder.tvQuantity.setText(String.valueOf(quantity));
            });

            holder.btnDecrease.setOnClickListener(v -> {
                int quantity = Integer.parseInt(holder.tvQuantity.getText().toString());
                if (quantity > 0) {
                    quantity--;
                    holder.tvQuantity.setText(String.valueOf(quantity));
                }
            });

            // Add to Cart button
            holder.btnAddToCart.setOnClickListener(v -> {
                int quantity = Integer.parseInt(holder.tvQuantity.getText().toString());
                if (quantity > 0) {
                    // Add logic to add product to cart
                    Toast.makeText(v.getContext(), product.getName() + " (Qty: " + quantity + ") added to cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "Please select a quantity", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvProductName, tvCategory, tvQuantity;
            ImageButton btnIncrease, btnDecrease;
            Button btnAddToCart;

            ViewHolder(View itemView) {
                super(itemView);
                tvProductName = itemView.findViewById(R.id.tvProductName);
                tvCategory = itemView.findViewById(R.id.tvCategory);
                tvQuantity = itemView.findViewById(R.id.tvQuantity);
                btnIncrease = itemView.findViewById(R.id.btnIncrease);
                btnDecrease = itemView.findViewById(R.id.btnDecrease);
                btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            }
        }
    }
}
package com.example.dogfoodapp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dogfoodapp.database.DatabaseHelper;



public class ManageProductActivity extends AppCompatActivity {
    private RecyclerView rvProducts;
    private ProductAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);

        dbHelper = new DatabaseHelper(this);
        rvProducts = findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));

        loadProducts();
    }

    private void loadProducts() {
        Cursor cursor = dbHelper.getAllProducts();
        adapter = new ProductAdapter(this, cursor);
        rvProducts.setAdapter(adapter);
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
        private Context context;
        private Cursor cursor;

        public ProductAdapter(Context context, Cursor cursor) {
            this.context = context;
            this.cursor = cursor;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_product, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (!cursor.moveToPosition(position)) return;

            final String productId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_PRODUCT_ID));
            final String productName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_PRODUCT_NAME));
            final double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_PRICE));
            final int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_QUANTITY));

            holder.tvProductName.setText(productName);
            holder.tvPrice.setText(String.format("Price: $%.2f", price));
            holder.tvQuantity.setText(String.format("Quantity: %d", quantity));

            holder.btnEdit.setOnClickListener(v -> showEditDialog(productId, productName, price, quantity));
            holder.btnDelete.setOnClickListener(v -> deleteProduct(productId));
        }

        @Override
        public int getItemCount() {
            return cursor.getCount();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvProductName, tvPrice, tvQuantity;
            ImageButton btnEdit, btnDelete;

            ViewHolder(View itemView) {
                super(itemView);
                tvProductName = itemView.findViewById(R.id.tvProductName);
                tvPrice = itemView.findViewById(R.id.tvPrice);
                tvQuantity = itemView.findViewById(R.id.tvQuantity);
                btnEdit = itemView.findViewById(R.id.btnEdit);
                btnDelete = itemView.findViewById(R.id.btnDelete);
            }
        }
    }

    private void showEditDialog(String productId, String currentName, double currentPrice, int currentQuantity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_product, null);

        EditText etNewName = view.findViewById(R.id.etNewName);
        EditText etNewPrice = view.findViewById(R.id.etNewPrice);
        EditText etNewQuantity = view.findViewById(R.id.etNewQuantity);

        etNewName.setText(currentName);
        etNewPrice.setText(String.valueOf(currentPrice));
        etNewQuantity.setText(String.valueOf(currentQuantity));

        builder.setView(view)
                .setTitle("Edit Product")
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = etNewName.getText().toString().trim();
                    double newPrice = Double.parseDouble(etNewPrice.getText().toString());
                    int newQuantity = Integer.parseInt(etNewQuantity.getText().toString());

                    if (!newName.isEmpty()) {
                        dbHelper.updateProduct(productId, newName, newPrice, newQuantity);
                        loadProducts();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteProduct(String productId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteProduct(productId);
                    loadProducts();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
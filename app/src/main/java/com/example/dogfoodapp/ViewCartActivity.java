package com.example.dogfoodapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewCartActivity extends AppCompatActivity {

    private RecyclerView rvCartItems;
    private TextView tvTotal;
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        rvCartItems = findViewById(R.id.rvCartItems);
        tvTotal = findViewById(R.id.tvTotal);

        setupRecyclerView();
        updateTotal();
    }

    private void setupRecyclerView() {
        List<CartItem> cartItems = Cart.getInstance().getItems();
        adapter = new CartAdapter(cartItems);
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        rvCartItems.setAdapter(adapter);
    }

//    private void updateTotal() {
//        double total = Cart.getInstance().getTotal();
//        tvTotal.setText(String.format("$%.2f", total));
//    }


    private void updateTotal() {
        double total = Cart.getInstance().getTotal();
        tvTotal.setText(String.format("$%.2f", total));
    }
    private class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
        private List<CartItem> cartItems;

        public CartAdapter(List<CartItem> cartItems) {
            this.cartItems = cartItems;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_cart, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            CartItem item = cartItems.get(position);
            Product product = item.getProduct();

            holder.tvProductName.setText(product.getName());
            holder.tvCategory.setText(product.getCategory());
            holder.tvPrice.setText(String.format("Price: $%.2f", product.getPrice()));
            holder.tvQuantity.setText(String.format("Qty: %d", item.getQuantity()));

            // Handle Edit Icon Click
            holder.btnEdit.setOnClickListener(v -> {
                holder.layoutEditQuantity.setVisibility(View.VISIBLE);
                holder.etQuantity.setText(String.valueOf(item.getQuantity()));
            });

            // Handle Save Button Click
            holder.btnSave.setOnClickListener(v -> {
                String newQuantityStr = holder.etQuantity.getText().toString().trim();
                if (!newQuantityStr.isEmpty()) {
                    int newQuantity = Integer.parseInt(newQuantityStr);
                    if (newQuantity > 0) {
                        item.setQuantity(newQuantity);
                        holder.tvQuantity.setText(String.format("Qty: %d", newQuantity));
                        holder.layoutEditQuantity.setVisibility(View.GONE);
                        updateTotal(); // Update the total price
                    } else {
                        Toast.makeText(ViewCartActivity.this, "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ViewCartActivity.this, "Please enter a quantity", Toast.LENGTH_SHORT).show();
                }
            });

            // Handle Remove Button Click
            holder.btnRemove.setOnClickListener(v -> {
                Cart.getInstance().removeItem(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                updateTotal();
            });
        }

        @Override
        public int getItemCount() {
            return cartItems.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvProductName, tvCategory, tvPrice, tvQuantity;
            ImageButton btnEdit;
            LinearLayout layoutEditQuantity;
            EditText etQuantity;
            Button btnSave, btnRemove;

            ViewHolder(View itemView) {
                super(itemView);
                tvProductName = itemView.findViewById(R.id.tvProductName);
                tvCategory = itemView.findViewById(R.id.tvCategory);
                tvPrice = itemView.findViewById(R.id.tvPrice);
                tvQuantity = itemView.findViewById(R.id.tvQuantity);
                btnEdit = itemView.findViewById(R.id.btnEdit);
                layoutEditQuantity = itemView.findViewById(R.id.layoutEditQuantity);
                etQuantity = itemView.findViewById(R.id.etQuantity);
                btnSave = itemView.findViewById(R.id.btnSave);
                btnRemove = itemView.findViewById(R.id.btnRemove);
            }
        }
    }
}
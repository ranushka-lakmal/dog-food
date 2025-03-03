package com.example.dogfoodapp;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogfoodapp.database.DatabaseHelper;

public class ManageCategoryActivity extends AppCompatActivity {
    private RecyclerView rvCategories;
    private CategoryAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);

        dbHelper = new DatabaseHelper(this);
        rvCategories = findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new LinearLayoutManager(this));

        loadCategories();
    }

    private void loadCategories() {
        Cursor cursor = dbHelper.getAllCategories();
        adapter = new CategoryAdapter(this, cursor);
        rvCategories.setAdapter(adapter);
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
        private Context context;
        private Cursor cursor;

        public CategoryAdapter(Context context, Cursor cursor) {
            this.context = context;
            this.cursor = cursor;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_category, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (!cursor.moveToPosition(position)) return;

            final String catId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CAT_ID));
            final String catName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CAT_NAME));

            holder.tvCategoryName.setText(catName);

            // Edit button click
            holder.btnEdit.setOnClickListener(v -> showEditDialog(catId, catName));

            // Delete button click
            holder.btnDelete.setOnClickListener(v -> deleteCategory(catId));
        }

        @Override
        public int getItemCount() {
            return cursor.getCount();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvCategoryName;
            ImageButton btnEdit, btnDelete;

            ViewHolder(View itemView) {
                super(itemView);
                tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
                btnEdit = itemView.findViewById(R.id.btnEdit);
                btnDelete = itemView.findViewById(R.id.btnDelete);
            }
        }
    }

    private void showEditDialog(String catId, String currentName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_category, null);
        EditText etNewName = view.findViewById(R.id.etNewName);

        etNewName.setText(currentName);

        builder.setView(view)
                .setTitle("Edit Category")
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = etNewName.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        dbHelper.updateCategory(catId, catId, newName);
                        loadCategories(); // Refresh the list
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteCategory(String catId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Category")
                .setMessage("Are you sure you want to delete this category?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteCategory(catId);
                    loadCategories(); // Refresh the list
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
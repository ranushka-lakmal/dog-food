package com.example.dogfoodapp;


import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dogfoodapp.database.DatabaseHelper;

public class ManageCategoryActivity extends AppCompatActivity {
    private ListView lvCategories;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_category_page);

        dbHelper = new DatabaseHelper(this);
        lvCategories = findViewById(R.id.lvCategories);

        loadCategories();
    }

    private void loadCategories() {
        Cursor cursor = dbHelper.getAllCategories();

        // Define columns and views for the adapter
        String[] fromColumns = {"cat_id", "cat_name"};
        int[] toViews = {android.R.id.text1, android.R.id.text2};

        // Create the adapter
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                cursor,
                fromColumns,
                toViews,
                0
        );

        lvCategories.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
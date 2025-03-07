package com.example.dogfoodapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dogfoodapp.database.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

public class EducationalContentActivity extends AppCompatActivity {

    private AutoCompleteTextView actvContentType, actvLifeStage;
    private TextInputEditText etTitle, etContent, etBreed;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> contentTypeAdapter, lifeStageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educational_content);

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        setupDropdowns();
        setupSubmitButton();
    }

    private void initializeViews() {
        actvContentType = findViewById(R.id.actvContentType);
        actvLifeStage = findViewById(R.id.actvLifeStage);
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        etBreed = findViewById(R.id.etBreed);
    }

    private void setupDropdowns() {
        // Content types
        String[] contentTypes = {"Article", "Video Guide", "Infographic", "Research Paper"};
        contentTypeAdapter = new ArrayAdapter<>(this, R.layout.list_item_dropdown, contentTypes);
        actvContentType.setAdapter(contentTypeAdapter);

        // Life stages
        String[] lifeStages = {"Puppy", "Adult", "Senior", "All Stages"};
        lifeStageAdapter = new ArrayAdapter<>(this, R.layout.list_item_dropdown, lifeStages);
        actvLifeStage.setAdapter(lifeStageAdapter);
    }

    private void setupSubmitButton() {
        findViewById(R.id.btnSubmit).setOnClickListener(v -> saveEducationalContent());
    }

    private void saveEducationalContent() {
        String type = actvContentType.getText().toString().trim();
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        String breed = etBreed.getText().toString().trim();
        String lifeStage = actvLifeStage.getText().toString().trim();

        if (validateInput(type, title, content)) {
            long result = dbHelper.addEducationalContent(
                    type,
                    title,
                    content,
                    breed.isEmpty() ? null : breed,
                    lifeStage.isEmpty() ? null : lifeStage
            );

            if (result != -1) {
                Toast.makeText(this, "Content saved successfully", Toast.LENGTH_SHORT).show();
                clearForm();
            } else {
                Toast.makeText(this, "Failed to save content", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInput(String type, String title, String content) {
        if (type.isEmpty()) {
            actvContentType.setError("Please select content type");
            return false;
        }
        if (title.isEmpty()) {
            etTitle.setError("Title is required");
            return false;
        }
        if (content.isEmpty()) {
            etContent.setError("Content is required");
            return false;
        }
        return true;
    }

    private void clearForm() {
        actvContentType.setText("");
        etTitle.setText("");
        etContent.setText("");
        etBreed.setText("");
        actvLifeStage.setText("");
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
package com.example.dogfoodapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogfoodapp.database.DatabaseHelper;

import java.util.List;

public class ManageEducationalContentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EducationalContentAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_educational);

        dbHelper = new DatabaseHelper(this);
        setupRecyclerView();
        setupAddButton();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.rvEducationalContent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadEducationalContent();
    }

    private void loadEducationalContent() {
        List<EducationalContent> contentList = dbHelper.getAllEducationalContent();
        adapter = new EducationalContentAdapter(contentList);
        recyclerView.setAdapter(adapter);
    }

    private void setupAddButton() {
        findViewById(R.id.fabAdd).setOnClickListener(v -> {
            startActivity(new Intent(this, EducationalContentActivity.class));
        });
    }

    private class EducationalContentAdapter extends RecyclerView.Adapter<EducationalContentAdapter.ViewHolder> {

        private final List<EducationalContent> contentList;

        public EducationalContentAdapter(List<EducationalContent> contentList) {
            this.contentList = contentList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_educational_manage, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            EducationalContent content = contentList.get(position);

            holder.tvTitle.setText(content.getTitle());
            holder.tvType.setText(content.getType());
            holder.tvBreed.setText(content.getBreed() != null ? content.getBreed() : "All Breeds");

            holder.btnEdit.setOnClickListener(v -> showEditDialog(content));
            holder.btnDelete.setOnClickListener(v -> deleteContent(content));
        }

        @Override
        public int getItemCount() {
            return contentList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvType, tvBreed;
            Button btnEdit, btnDelete;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvType = itemView.findViewById(R.id.tvType);
                tvBreed = itemView.findViewById(R.id.tvBreed);
                btnEdit = itemView.findViewById(R.id.btnEdit);
                btnDelete = itemView.findViewById(R.id.btnDelete);
            }
        }
    }

    private void showEditDialog(EducationalContent content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_educational, null);

        View includedLayout = view.findViewById(R.id.includedForm);
        Button submitButton = includedLayout.findViewById(R.id.btnSubmit);
        submitButton.setVisibility(View.GONE);

        AutoCompleteTextView actvContentType = includedLayout.findViewById(R.id.actvContentType);
        EditText etTitle = includedLayout.findViewById(R.id.etTitle);
        EditText etContent = includedLayout.findViewById(R.id.etContent);
        EditText etBreed = includedLayout.findViewById(R.id.etBreed);
        AutoCompleteTextView actvLifeStage = includedLayout.findViewById(R.id.actvLifeStage);

        actvContentType.setText(content.getType()); // Use corrected variable name
        etTitle.setText(content.getTitle());
        etContent.setText(content.getContent());
        etBreed.setText(content.getBreed());
        actvLifeStage.setText(content.getLifeStage());

        builder.setView(view)
                .setTitle("Edit Content")
                .setPositiveButton("Save", (dialog, which) -> {
                    EducationalContent updatedContent = new EducationalContent(
                            etTitle.getText().toString(),
                            actvContentType.getText().toString(), // Updated here
                            etContent.getText().toString(),
                            etBreed.getText().toString(),
                            actvLifeStage.getText().toString()
                    );
                    dbHelper.updateEducationalContent(content.getTitle(), updatedContent);
                    loadEducationalContent();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteContent(EducationalContent content) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Content")
                .setMessage("Are you sure you want to delete this content?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteEducationalContent(content.getTitle());
                    loadEducationalContent();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEducationalContent();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
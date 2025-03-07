package com.example.dogfoodapp;

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

import java.util.List;

public class ViewEducationalActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EducationalContentAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_educational);

        dbHelper = new DatabaseHelper(this);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.rvEducationalContent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<EducationalContent> contentList = dbHelper.getAllEducationalContent();
        adapter = new EducationalContentAdapter(contentList);
        recyclerView.setAdapter(adapter);
    }


    private void showFilterDialog() {
        // Implement filter logic here
        Toast.makeText(this, "Filter functionality coming soon!", Toast.LENGTH_SHORT).show();
    }

    private static class EducationalContentAdapter extends RecyclerView.Adapter<EducationalContentAdapter.ViewHolder> {

        private final List<EducationalContent> contentList;

        public EducationalContentAdapter(List<EducationalContent> contentList) {
            this.contentList = contentList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_educational_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            EducationalContent content = contentList.get(position);

            holder.tvTitle.setText(content.getTitle());
            holder.tvType.setText(content.getType());
            holder.tvContent.setText(content.getContent());

            // Handle optional fields
            if (content.getBreed() != null && !content.getBreed().isEmpty()) {
                holder.tvBreed.setText(content.getBreed());
                holder.tvBreed.setVisibility(View.VISIBLE);
            } else {
                holder.tvBreed.setVisibility(View.GONE);
            }

            if (content.getLifeStage() != null && !content.getLifeStage().isEmpty()) {
                holder.tvLifeStage.setText(content.getLifeStage());
                holder.tvLifeStage.setVisibility(View.VISIBLE);
            } else {
                holder.tvLifeStage.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return contentList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvType, tvContent, tvBreed, tvLifeStage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvType = itemView.findViewById(R.id.tvType);
                tvContent = itemView.findViewById(R.id.tvContent);
                tvBreed = itemView.findViewById(R.id.tvBreed);
                tvLifeStage = itemView.findViewById(R.id.tvLifeStage);
            }
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
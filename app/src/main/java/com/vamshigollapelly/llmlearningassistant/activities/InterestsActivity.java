package com.vamshigollapelly.llmlearningassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vamshigollapelly.llmlearningassistant.R;
import com.vamshigollapelly.llmlearningassistant.adapters.InterestsAdapter;
import com.vamshigollapelly.llmlearningassistant.utils.DummyData;

import java.util.ArrayList;

public class InterestsActivity extends AppCompatActivity {

    private RecyclerView rvInterests;
    private Button btnNext;
    private InterestsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        rvInterests = findViewById(R.id.rvInterests);
        btnNext = findViewById(R.id.btnNext);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        btnNext.startAnimation(fadeIn);

        adapter = new InterestsAdapter(DummyData.getTopics());

        rvInterests.setLayoutManager(new GridLayoutManager(this, 2));
        rvInterests.setAdapter(adapter);

        btnNext.setOnClickListener(v -> {
            ArrayList<String> selected = new ArrayList<>(adapter.getSelectedItems());

            if (selected.isEmpty()) {
                Toast.makeText(this, "Select at least one interest", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(InterestsActivity.this, HomeActivity.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            intent.putStringArrayListExtra("topics", selected);
            startActivity(intent);
        });
    }
}
package com.vamshigollapelly.llmlearningassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vamshigollapelly.llmlearningassistant.R;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private TextView tvHello, tvUserName, tvHomeTaskDescription;
    private ImageView ivProfile, ivNotification;
    private Button btnStartTask, btnOpenFlashcards, btnOpenStudyPlan;

    private ArrayList<String> selectedTopics;
    private String username;
    private String firstTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvHello = findViewById(R.id.tvHello);
        tvUserName = findViewById(R.id.tvUserName);
        tvHomeTaskDescription = findViewById(R.id.tvHomeTaskDescription);

        ivProfile = findViewById(R.id.ivProfile);
        ivNotification = findViewById(R.id.ivNotification);
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);

        findViewById(R.id.btnStartTask).startAnimation(fadeIn);
        findViewById(R.id.btnOpenFlashcards).startAnimation(fadeIn);
        findViewById(R.id.btnOpenStudyPlan).startAnimation(fadeIn);

        btnStartTask = findViewById(R.id.btnStartTask);
        btnOpenFlashcards = findViewById(R.id.btnOpenFlashcards);
        btnOpenStudyPlan = findViewById(R.id.btnOpenStudyPlan);

        username = getIntent().getStringExtra("username");
        if (username == null || username.trim().isEmpty()) {
            username = "Student";
        }

        selectedTopics = getIntent().getStringArrayListExtra("topics");
        if (selectedTopics == null) {
            selectedTopics = new ArrayList<>();
        }

        firstTopic = selectedTopics.isEmpty() ? "Algorithms" : selectedTopics.get(0);

        tvHello.setText("Hello,");
        tvUserName.setText(username);
        tvHomeTaskDescription.setText("Practice task based on your interest: " + firstTopic);


        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        tvHello.startAnimation(fadeIn);
        tvUserName.startAnimation(fadeIn);
        btnStartTask.startAnimation(slideUp);
        btnOpenFlashcards.startAnimation(slideUp);
        btnOpenStudyPlan.startAnimation(slideUp);

        ivProfile.setOnClickListener(v ->
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
        );

        ivNotification.setOnClickListener(v ->
                Toast.makeText(this, "You have 1 learning task due", Toast.LENGTH_SHORT).show()
        );

        btnStartTask.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, TaskActivity.class);
            intent.putExtra("taskTitle", "Generated Task 1");
            intent.putExtra("taskDescription", "Practice task based on your interest: " + firstTopic);
            startActivity(intent);
        });

        btnOpenFlashcards.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, FlashcardActivity.class);
            intent.putExtra("topic", firstTopic);
            startActivity(intent);
        });

        btnOpenStudyPlan.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, StudyPlanActivity.class);
            intent.putStringArrayListExtra("topics", selectedTopics);
            startActivity(intent);
        });
        btnOpenFlashcards.setOnClickListener(v -> {
            btnOpenFlashcards.setText("Generating...");
            btnOpenFlashcards.setEnabled(false);

            v.postDelayed(() -> {
                startActivity(new Intent(this, FlashcardActivity.class));
            }, 1000);
        });

    }
}
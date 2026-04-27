package com.vamshigollapelly.llmlearningassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vamshigollapelly.llmlearningassistant.R;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private TextView tvHello, tvUserName, tvHomeTaskDescription;
    private EditText etCustomTopic;
    private ImageView ivProfile, ivNotification;
    private Button btnStartTask, btnOpenFlashcards, btnOpenStudyPlan;

    private ArrayList<String> selectedTopics;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvHello = findViewById(R.id.tvHello);
        tvUserName = findViewById(R.id.tvUserName);
        tvHomeTaskDescription = findViewById(R.id.tvHomeTaskDescription);
        etCustomTopic = findViewById(R.id.etCustomTopic);

        ivProfile = findViewById(R.id.ivProfile);
        ivNotification = findViewById(R.id.ivNotification);

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

        String suggestedTopic = "";
        if (!selectedTopics.isEmpty()) {
            suggestedTopic = selectedTopics.get(0);
        }

        tvHello.setText("Hello,");
        tvUserName.setText(username);
        tvHomeTaskDescription.setText("Enter a topic below and start your learning task.");

        etCustomTopic.setHint("Enter topic, e.g. OOP, Database, AI");
        etCustomTopic.setText(suggestedTopic);

        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
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
            String topic = getTopicFromInput();

            if (topic.isEmpty()) {
                etCustomTopic.setError("Please enter a topic");
                etCustomTopic.requestFocus();
                return;
            }

            Intent intent = new Intent(HomeActivity.this, TaskActivity.class);
            intent.putExtra("taskTitle", "Generated Task: " + topic);
            intent.putExtra("taskDescription", "Practice task based on your topic: " + topic);
            intent.putExtra("topic", topic);
            startActivity(intent);
        });

        btnOpenFlashcards.setOnClickListener(v -> {
            String topic = getTopicFromInput();

            if (topic.isEmpty()) {
                etCustomTopic.setError("Please enter a topic for flashcards");
                etCustomTopic.requestFocus();
                return;
            }

            Intent intent = new Intent(HomeActivity.this, FlashcardActivity.class);
            intent.putExtra("topic", topic);
            startActivity(intent);
        });
        btnOpenStudyPlan.setOnClickListener(v -> {
            String topic = getTopicFromInput();

            if (topic.isEmpty()) {
                etCustomTopic.setError("Please enter a topic for study plan");
                etCustomTopic.requestFocus();
                return;
            }

            ArrayList<String> topicsForPlan = new ArrayList<>();
            topicsForPlan.add(topic);

            Intent intent = new Intent(HomeActivity.this, StudyPlanActivity.class);
            intent.putStringArrayListExtra("topics", topicsForPlan);
            startActivity(intent);
        });
    }

    private String getTopicFromInput() {
        if (etCustomTopic == null) {
            return "";
        }
        return etCustomTopic.getText().toString().trim();
    }
}
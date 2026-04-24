package com.vamshigollapelly.llmlearningassistant.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vamshigollapelly.llmlearningassistant.R;

import java.util.ArrayList;

public class StudyPlanActivity extends AppCompatActivity {

    private TextView tvStudyHistory, tvStudyPlanPrompt, tvStudyPlanResponse, tvStudyPlanError;
    private Button btnGenerateStudyPlan;
    private ProgressBar progressStudyPlan;
    private ArrayList<String> selectedTopics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_plan);

        tvStudyHistory = findViewById(R.id.tvStudyHistory);
        btnGenerateStudyPlan = findViewById(R.id.btnGenerateStudyPlan);
        tvStudyPlanPrompt = findViewById(R.id.tvStudyPlanPrompt);
        tvStudyPlanResponse = findViewById(R.id.tvStudyPlanResponse);
        tvStudyPlanError = findViewById(R.id.tvStudyPlanError);
        progressStudyPlan = findViewById(R.id.progressStudyPlan);

        selectedTopics = getIntent().getStringArrayListExtra("topics");
        if (selectedTopics == null || selectedTopics.isEmpty()) {
            selectedTopics = new ArrayList<>();
            selectedTopics.add("Algorithms");
        }

        tvStudyHistory.setText("History: You selected " + getTopicsText()
                + ". You completed quiz practice and reviewed answer explanations.");

        btnGenerateStudyPlan.setOnClickListener(v -> generateStudyPlan());
    }

    private String getTopicsText() {
        return selectedTopics.toString().replace("[", "").replace("]", "");
    }

    private void generateStudyPlan() {
        String topicsText = getTopicsText();

        String prompt = "Suggest a simple 7-day study plan based on this student history: "
                + "Selected topics: " + topicsText
                + ". Completed quiz practice and reviewed answer explanations.";

        tvStudyPlanPrompt.setText("Prompt:\n" + prompt);
        tvStudyPlanPrompt.setVisibility(View.VISIBLE);

        progressStudyPlan.setVisibility(View.VISIBLE);
        tvStudyPlanResponse.setVisibility(View.GONE);
        tvStudyPlanError.setVisibility(View.GONE);

        tvStudyPlanResponse.postDelayed(() -> {
            progressStudyPlan.setVisibility(View.GONE);

            String response =
                    "AI Response:\n\n"
                            + "Day 1: Review the basics of " + topicsText + ".\n"
                            + "Day 2: Watch or read one short lesson and write key notes.\n"
                            + "Day 3: Practise 3 beginner-level questions.\n"
                            + "Day 4: Revise mistakes from previous quiz attempts.\n"
                            + "Day 5: Create flashcards and test yourself.\n"
                            + "Day 6: Complete a small practice task.\n"
                            + "Day 7: Take a short quiz and review weak areas.";

            tvStudyPlanResponse.setText(response);
            tvStudyPlanResponse.setVisibility(View.VISIBLE);
        }, 1200);
    }
}
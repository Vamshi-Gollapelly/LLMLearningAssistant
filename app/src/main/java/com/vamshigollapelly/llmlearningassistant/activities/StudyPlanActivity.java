package com.vamshigollapelly.llmlearningassistant.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vamshigollapelly.llmlearningassistant.BuildConfig;
import com.vamshigollapelly.llmlearningassistant.R;
import com.vamshigollapelly.llmlearningassistant.network.ApiClient;
import com.vamshigollapelly.llmlearningassistant.network.ChatRequest;
import com.vamshigollapelly.llmlearningassistant.network.ChatResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyPlanActivity extends AppCompatActivity {

    private TextView tvStudyHistory, tvStudyPlanPrompt, tvStudyPlanResponse, tvStudyPlanError;
    private Button btnGenerateStudyPlan;
    private ProgressBar progressStudyPlan;
    private ArrayList<String> selectedTopics;

    private static final String API_KEY = BuildConfig.OPENAI_API_KEY;

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

        String prompt = "Create a simple 7-day study plan for a student based on these selected topics: "
                + topicsText + ". Keep each day short, practical, and easy to follow.";

        tvStudyPlanPrompt.setText("Prompt:\n" + prompt);
        tvStudyPlanPrompt.setVisibility(View.VISIBLE);

        progressStudyPlan.setVisibility(View.VISIBLE);
        tvStudyPlanResponse.setVisibility(View.GONE);
        tvStudyPlanError.setVisibility(View.GONE);

        if (API_KEY == null || API_KEY.trim().isEmpty()) {
            progressStudyPlan.setVisibility(View.GONE);
            tvStudyPlanError.setText("API key is missing. Please add it in local.properties.");
            tvStudyPlanError.setVisibility(View.VISIBLE);
            return;
        }

        ChatRequest request = new ChatRequest("gpt-4.1-mini", prompt);

        ApiClient.getApiService().getLearningResponse(
                "Bearer " + API_KEY,
                "application/json",
                request
        ).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                progressStudyPlan.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    tvStudyPlanResponse.setText("AI Response:\n\n" + response.body().getTextResponse());
                    tvStudyPlanResponse.setVisibility(View.VISIBLE);
                } else {
                    tvStudyPlanError.setText("Failed to generate study plan. Please try again.");
                    tvStudyPlanError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                progressStudyPlan.setVisibility(View.GONE);
                tvStudyPlanError.setText("Network error: " + t.getMessage());
                tvStudyPlanError.setVisibility(View.VISIBLE);
            }
        });
    }
}
package com.vamshigollapelly.llmlearningassistant.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vamshigollapelly.llmlearningassistant.BuildConfig;
import com.vamshigollapelly.llmlearningassistant.R;
import com.vamshigollapelly.llmlearningassistant.network.ApiClient;
import com.vamshigollapelly.llmlearningassistant.network.ChatRequest;
import com.vamshigollapelly.llmlearningassistant.network.ChatResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlashcardActivity extends AppCompatActivity {

    private EditText etFlashcardTopic;
    private Button btnGenerateFlashcards;
    private TextView tvFlashcardPrompt, tvFlashcardResponse, tvFlashcardError;
    private ProgressBar progressFlashcards;

    private static final String API_KEY = BuildConfig.OPENAI_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        etFlashcardTopic = findViewById(R.id.etFlashcardTopic);
        btnGenerateFlashcards = findViewById(R.id.btnGenerateFlashcards);
        tvFlashcardPrompt = findViewById(R.id.tvFlashcardPrompt);
        tvFlashcardResponse = findViewById(R.id.tvFlashcardResponse);
        tvFlashcardError = findViewById(R.id.tvFlashcardError);
        progressFlashcards = findViewById(R.id.progressFlashcards);

        String topic = getIntent().getStringExtra("topic");

        etFlashcardTopic.setHint("Enter topic, e.g. OOP, Database, AI");

        if (topic != null && !topic.trim().isEmpty()) {
            etFlashcardTopic.setText(topic);
        } else {
            etFlashcardTopic.setText("");
        }

        btnGenerateFlashcards.setOnClickListener(v -> generateFlashcards());
    }

    private void generateFlashcards() {
        String topic = etFlashcardTopic.getText().toString().trim();

        if (topic.isEmpty()) {
            etFlashcardTopic.setError("Please enter a topic");
            etFlashcardTopic.requestFocus();
            return;
        }

        String prompt = "Create exactly 3 short student-friendly flashcards for this topic: "
                + topic
                + ". Format each flashcard as Q and A. Keep the wording simple and useful for learning.";

        tvFlashcardPrompt.setText("Prompt:\n" + prompt);
        tvFlashcardPrompt.setVisibility(View.VISIBLE);

        progressFlashcards.setVisibility(View.VISIBLE);
        tvFlashcardResponse.setVisibility(View.GONE);
        tvFlashcardError.setVisibility(View.GONE);

        btnGenerateFlashcards.setEnabled(false);
        btnGenerateFlashcards.setText("Generating...");

        if (API_KEY == null || API_KEY.trim().isEmpty()) {
            resetButton();
            progressFlashcards.setVisibility(View.GONE);
            tvFlashcardError.setText("API key is missing. Please add it in local.properties.");
            tvFlashcardError.setVisibility(View.VISIBLE);
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
                resetButton();
                progressFlashcards.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    String aiText = response.body().getTextResponse();

                    if (aiText == null || aiText.trim().isEmpty()) {
                        tvFlashcardError.setText("The AI response was empty. Please try again.");
                        tvFlashcardError.setVisibility(View.VISIBLE);
                    } else {
                        tvFlashcardResponse.setText("AI Response:\n\n" + aiText);
                        tvFlashcardResponse.setVisibility(View.VISIBLE);
                    }
                } else {
                    showApiError(response);
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                resetButton();
                progressFlashcards.setVisibility(View.GONE);
                tvFlashcardError.setText("Network error:\n" + t.getMessage());
                tvFlashcardError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showApiError(Response<ChatResponse> response) {
        try {
            String errorBody = response.errorBody() != null
                    ? response.errorBody().string()
                    : "No error details returned.";

            tvFlashcardError.setText("API Error " + response.code() + ":\n" + errorBody);
        } catch (Exception e) {
            tvFlashcardError.setText("Failed to generate flashcards. Error code: " + response.code());
        }

        tvFlashcardError.setVisibility(View.VISIBLE);
    }

    private void resetButton() {
        btnGenerateFlashcards.setEnabled(true);
        btnGenerateFlashcards.setText("Generate Flashcards");
    }
}
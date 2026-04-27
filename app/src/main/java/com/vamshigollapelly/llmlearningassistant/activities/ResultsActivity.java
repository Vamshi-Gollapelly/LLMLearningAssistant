package com.vamshigollapelly.llmlearningassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vamshigollapelly.llmlearningassistant.R;
import com.vamshigollapelly.llmlearningassistant.adapters.ResultsAdapter;
import com.vamshigollapelly.llmlearningassistant.models.QuizResult;
import com.vamshigollapelly.llmlearningassistant.network.ApiClient;
import com.vamshigollapelly.llmlearningassistant.network.ChatRequest;
import com.vamshigollapelly.llmlearningassistant.network.ChatResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultsActivity extends AppCompatActivity {

    private TextView tvResultsTitle, tvScore;
    private RecyclerView rvResults;
    private Button btnContinue;
    private List<QuizResult> resultsList;

    private static final String API_KEY = "PASTE_YOUR_OPENAI_API_KEY_HERE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        tvResultsTitle = findViewById(R.id.tvResultsTitle);
        tvScore = findViewById(R.id.tvScore);
        rvResults = findViewById(R.id.rvResults);
        btnContinue = findViewById(R.id.btnContinue);

        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 0);

        tvScore.setText("Score: " + score + "/" + total);

        resultsList = TaskActivity.ResultsHolder.results;

        if (resultsList == null) {
            resultsList = new ArrayList<>();
        }

        rvResults.setLayoutManager(new LinearLayoutManager(this));
        rvResults.setAdapter(new ResultsAdapter(resultsList, this::generateExplanation));

        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(ResultsActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void generateExplanation(
            QuizResult result,
            TextView tvPrompt,
            TextView tvResponse,
            TextView tvError,
            ProgressBar progressBar
    ) {
        String prompt = "Explain in simple words why this answer is correct or incorrect.\n"
                + "Question: " + result.getQuestion() + "\n"
                + "Student answer: " + result.getSelectedAnswer() + "\n"
                + "Correct answer: " + result.getCorrectAnswer();

        tvPrompt.setText("Prompt:\n" + prompt);
        tvPrompt.setVisibility(TextView.VISIBLE);

        progressBar.setVisibility(ProgressBar.VISIBLE);
        tvResponse.setVisibility(TextView.GONE);
        tvError.setVisibility(TextView.GONE);

        generateExplanationFromLLM(prompt, tvResponse, tvError, progressBar);
    }

    private void generateExplanationFromLLM(
            String prompt,
            TextView tvResponse,
            TextView tvError,
            ProgressBar progressBar
    ) {
        if (API_KEY.equals("PASTE_YOUR_OPENAI_API_KEY_HERE")) {
            progressBar.setVisibility(ProgressBar.GONE);
            tvError.setText("API key is missing. Please add your OpenAI API key.");
            tvError.setVisibility(TextView.VISIBLE);
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
                progressBar.setVisibility(ProgressBar.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    tvResponse.setText("AI Response:\n" + response.body().getTextResponse());
                    tvResponse.setVisibility(TextView.VISIBLE);
                } else {
                    tvError.setText("Failed to generate explanation. Please try again.");
                    tvError.setVisibility(TextView.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                progressBar.setVisibility(ProgressBar.GONE);
                tvError.setText("Network error: " + t.getMessage());
                tvError.setVisibility(TextView.VISIBLE);
            }
        });
    }
}
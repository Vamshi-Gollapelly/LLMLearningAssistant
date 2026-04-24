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

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    private TextView tvResultsTitle, tvScore;
    private RecyclerView rvResults;
    private Button btnContinue;
    private List<QuizResult> resultsList;

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
        rvResults.setAdapter(new ResultsAdapter(resultsList, this::generateDummyExplanation));

        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(ResultsActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void generateDummyExplanation(
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

        tvResponse.postDelayed(() -> {
            progressBar.setVisibility(ProgressBar.GONE);

            String explanation;

            if (result.isCorrect()) {
                explanation = "AI Response:\nYour answer is correct because it matches the key concept tested in this question. This shows that you understood the main idea.";
            } else {
                explanation = "AI Response:\nYour answer is incorrect. The correct answer is '" + result.getCorrectAnswer()
                        + "' because it better matches the concept asked in the question. Review this topic once more and try a similar question.";
            }

            tvResponse.setText(explanation);
            tvResponse.setVisibility(TextView.VISIBLE);
        }, 1200);
    }
}
package com.vamshigollapelly.llmlearningassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vamshigollapelly.llmlearningassistant.R;
import com.vamshigollapelly.llmlearningassistant.models.QuizResult;

public class ProfileActivity extends AppCompatActivity {

    private TextView backButton, usernameText, totalQuestionsText, correctAnswersText, incorrectAnswersText;

    private String finalUsername = "Student";
    private int finalTotal = 0;
    private int finalCorrect = 0;
    private int finalIncorrect = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        backButton = findViewById(R.id.backButton);
        usernameText = findViewById(R.id.usernameText);
        totalQuestionsText = findViewById(R.id.totalQuestionsText);
        correctAnswersText = findViewById(R.id.correctAnswersText);
        incorrectAnswersText = findViewById(R.id.incorrectAnswersText);

        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        String receivedUsername = getIntent().getStringExtra("username");
        if (receivedUsername != null && !receivedUsername.trim().isEmpty()) {
            finalUsername = receivedUsername;
        }

        if (TaskActivity.ResultsHolder.results != null) {
            finalTotal = TaskActivity.ResultsHolder.results.size();

            for (QuizResult result : TaskActivity.ResultsHolder.results) {
                if (result.isCorrect()) {
                    finalCorrect++;
                }
            }

            finalIncorrect = finalTotal - finalCorrect;
        }

        usernameText.setText(finalUsername);
        totalQuestionsText.setText(String.valueOf(finalTotal));
        correctAnswersText.setText(String.valueOf(finalCorrect));
        incorrectAnswersText.setText(String.valueOf(finalIncorrect));
    }

    public void openHistory(View view) {
        Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
        startActivity(intent);
    }

    public void openUpgrade(View view) {
        Intent intent = new Intent(ProfileActivity.this, UpgradeActivity.class);
        startActivity(intent);
    }

    public void openShareProfile(View view) {
        Intent intent = new Intent(ProfileActivity.this, ShareProfileActivity.class);
        intent.putExtra("username", finalUsername);
        intent.putExtra("total", finalTotal);
        intent.putExtra("correct", finalCorrect);
        intent.putExtra("incorrect", finalIncorrect);
        startActivity(intent);
    }
}
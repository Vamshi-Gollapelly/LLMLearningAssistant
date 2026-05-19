package com.vamshigollapelly.llmlearningassistant.activities;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vamshigollapelly.llmlearningassistant.R;
import com.vamshigollapelly.llmlearningassistant.models.QuizResult;

public class HistoryActivity extends AppCompatActivity {

    private LinearLayout historyContainer;
    private TextView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        backButton = findViewById(R.id.backButton);
        historyContainer = findViewById(R.id.historyContainer);

        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        if (historyContainer == null) {
            return;
        }

        if (TaskActivity.ResultsHolder.results == null ||
                TaskActivity.ResultsHolder.results.isEmpty()) {

            addHistoryItem("No quiz history available yet.\n\nComplete a quiz first, then come back to this screen.");
            return;
        }

        int count = 1;

        for (QuizResult result : TaskActivity.ResultsHolder.results) {
            String historyText =
                    count + ". " + result.getQuestion() + "\n\n" +
                            "Your Answer: " + result.getSelectedAnswer() + "\n" +
                            "Correct Answer: " + result.getCorrectAnswer() + "\n" +
                            "Result: " + (result.isCorrect() ? "Correct" : "Incorrect");

            addHistoryItem(historyText);
            count++;
        }
    }

    private void addHistoryItem(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(15);
        textView.setTextColor(getResources().getColor(android.R.color.white));
        textView.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
        textView.setPadding(20, 20, 20, 20);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(0, 0, 0, 16);
        textView.setLayoutParams(params);

        historyContainer.addView(textView);
    }
}
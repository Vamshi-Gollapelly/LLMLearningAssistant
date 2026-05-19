package com.vamshigollapelly.llmlearningassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vamshigollapelly.llmlearningassistant.R;

public class ShareProfileActivity extends AppCompatActivity {

    private TextView backButton, shareProfileText;
    private Button shareNowButton;
    private String profileSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_profile);

        backButton = findViewById(R.id.backButton);
        shareProfileText = findViewById(R.id.shareProfileText);
        shareNowButton = findViewById(R.id.shareNowButton);

        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        String username = getIntent().getStringExtra("username");
        if (username == null || username.trim().isEmpty()) {
            username = "Student";
        }

        int total = getIntent().getIntExtra("total", 0);
        int correct = getIntent().getIntExtra("correct", 0);
        int incorrect = getIntent().getIntExtra("incorrect", 0);

        profileSummary =
                "Learning Assistant Profile\n\n" +
                        "Username: " + username + "\n" +
                        "Total Questions: " + total + "\n" +
                        "Correct Answers: " + correct + "\n" +
                        "Incorrect Answers: " + incorrect + "\n\n" +
                        "Shared from LLM Learning Assistant App.";

        if (shareProfileText != null) {
            shareProfileText.setText(profileSummary);
        }

        if (shareNowButton != null) {
            shareNowButton.setOnClickListener(v -> {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Learning Progress");
                shareIntent.putExtra(Intent.EXTRA_TEXT, profileSummary);

                startActivity(Intent.createChooser(shareIntent, "Share Profile Using"));
            });
        }
    }
}
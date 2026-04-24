package com.vamshigollapelly.llmlearningassistant.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vamshigollapelly.llmlearningassistant.R;

public class FlashcardActivity extends AppCompatActivity {

    private EditText etFlashcardTopic;
    private Button btnGenerateFlashcards;
    private TextView tvFlashcardPrompt, tvFlashcardResponse, tvFlashcardError;
    private ProgressBar progressFlashcards;

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
        if (topic == null || topic.trim().isEmpty()) {
            topic = "Algorithms";
        }
        etFlashcardTopic.setText(topic);

        btnGenerateFlashcards.setOnClickListener(v -> generateFlashcards());
    }

    private void generateFlashcards() {
        String topic = etFlashcardTopic.getText().toString().trim();

        if (topic.isEmpty()) {
            etFlashcardTopic.setError("Enter a topic first");
            etFlashcardTopic.requestFocus();
            return;
        }

        String prompt = "Create exactly 3 short flashcards from this learning topic: " + topic
                + ". Each flashcard should have a question and answer.";

        tvFlashcardPrompt.setText("Prompt:\n" + prompt);
        tvFlashcardPrompt.setVisibility(View.VISIBLE);

        progressFlashcards.setVisibility(View.VISIBLE);
        tvFlashcardResponse.setVisibility(View.GONE);
        tvFlashcardError.setVisibility(View.GONE);

        tvFlashcardResponse.postDelayed(() -> {
            progressFlashcards.setVisibility(View.GONE);

            String response =
                    "AI Response:\n\n"
                            + "Flashcard 1\n"
                            + "Q: What is " + topic + "?\n"
                            + "A: It is an important concept used to solve or understand computing problems.\n\n"
                            + "Flashcard 2\n"
                            + "Q: Why should students learn " + topic + "?\n"
                            + "A: It helps build stronger problem-solving and technical understanding.\n\n"
                            + "Flashcard 3\n"
                            + "Q: How can you revise " + topic + "?\n"
                            + "A: Practise examples, explain the idea in your own words, and complete small quizzes.";

            tvFlashcardResponse.setText(response);
            tvFlashcardResponse.setVisibility(View.VISIBLE);
        }, 1200);
    }
}
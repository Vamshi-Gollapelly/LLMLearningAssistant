package com.vamshigollapelly.llmlearningassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vamshigollapelly.llmlearningassistant.BuildConfig;
import com.vamshigollapelly.llmlearningassistant.R;
import com.vamshigollapelly.llmlearningassistant.adapters.QuestionAdapter;
import com.vamshigollapelly.llmlearningassistant.models.QuizQuestion;
import com.vamshigollapelly.llmlearningassistant.models.QuizResult;
import com.vamshigollapelly.llmlearningassistant.network.ApiClient;
import com.vamshigollapelly.llmlearningassistant.network.ChatRequest;
import com.vamshigollapelly.llmlearningassistant.network.ChatResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskActivity extends AppCompatActivity {

    private TextView tvTaskTitle, tvTaskDescription;
    private RecyclerView rvQuestions;
    private Button btnSubmitTask;

    private final List<QuizQuestion> questionList = new ArrayList<>();

    private static final String API_KEY = BuildConfig.OPENAI_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        tvTaskTitle = findViewById(R.id.tvTaskTitle);
        tvTaskDescription = findViewById(R.id.tvTaskDescription);
        rvQuestions = findViewById(R.id.rvQuestions);
        btnSubmitTask = findViewById(R.id.btnSubmitTask);

        String taskTitle = getIntent().getStringExtra("taskTitle");
        String taskDescription = getIntent().getStringExtra("taskDescription");
        String topic = getIntent().getStringExtra("topic");

        if (topic == null || topic.trim().isEmpty()) {
            topic = "General Knowledge";
        }

        if (taskTitle == null || taskTitle.trim().isEmpty()) {
            taskTitle = "Generated Task: " + topic;
        }

        if (taskDescription == null || taskDescription.trim().isEmpty()) {
            taskDescription = "Practice task based on your topic: " + topic;
        }

        tvTaskTitle.setText(taskTitle);
        tvTaskDescription.setText(taskDescription);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        tvTaskTitle.startAnimation(fadeIn);
        tvTaskDescription.startAnimation(fadeIn);

        rvQuestions.setLayoutManager(new LinearLayoutManager(this));

        btnSubmitTask.setEnabled(false);
        btnSubmitTask.setText("Loading Questions...");

        generateQuiz(topic);

        btnSubmitTask.setOnClickListener(v -> submitTask());
    }

    private void generateQuiz(String topic) {
        if (API_KEY == null || API_KEY.trim().isEmpty()) {
            loadFallbackQuestions(topic);
            Toast.makeText(this, "API key missing. Showing fallback questions.", Toast.LENGTH_SHORT).show();
            return;
        }

        String prompt =
                "Create exactly 3 multiple-choice quiz questions for this learning topic: " + topic + ".\n" +
                        "Return only in this exact format:\n\n" +
                        "Q: Question text\n" +
                        "A) Option one\n" +
                        "B) Option two\n" +
                        "C) Option three\n" +
                        "D) Option four\n" +
                        "Answer: Correct option text\n\n" +
                        "Q: Question text\n" +
                        "A) Option one\n" +
                        "B) Option two\n" +
                        "C) Option three\n" +
                        "D) Option four\n" +
                        "Answer: Correct option text\n\n" +
                        "Keep every question specific to the topic. Do not include explanations.";

        ChatRequest request = new ChatRequest("gpt-4.1-mini", prompt);

        ApiClient.getApiService().getLearningResponse(
                "Bearer " + API_KEY,
                "application/json",
                request
        ).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    parseQuiz(response.body().getTextResponse(), topic);
                } else {
                    loadFallbackQuestions(topic);
                    Toast.makeText(TaskActivity.this, "Could not load AI quiz. Showing fallback questions.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                loadFallbackQuestions(topic);
                Toast.makeText(TaskActivity.this, "Network issue. Showing fallback questions.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void parseQuiz(String text, String topic) {
        questionList.clear();

        if (text == null || text.trim().isEmpty()) {
            loadFallbackQuestions(topic);
            return;
        }

        String[] blocks = text.split("Q:");

        for (String block : blocks) {
            block = block.trim();

            if (block.isEmpty()) {
                continue;
            }

            String[] lines = block.split("\\r?\\n");

            if (lines.length < 6) {
                continue;
            }

            String question = lines[0].trim();
            String optionA = cleanOption(lines[1], "A)");
            String optionB = cleanOption(lines[2], "B)");
            String optionC = cleanOption(lines[3], "C)");
            String optionD = cleanOption(lines[4], "D)");
            String answer = cleanAnswer(lines[5]);

            if (!question.isEmpty()
                    && !optionA.isEmpty()
                    && !optionB.isEmpty()
                    && !optionC.isEmpty()
                    && !optionD.isEmpty()
                    && !answer.isEmpty()) {

                questionList.add(new QuizQuestion(
                        questionList.size() + 1,
                        question,
                        Arrays.asList(optionA, optionB, optionC, optionD),
                        answer
                ));
            }
        }

        if (questionList.isEmpty()) {
            loadFallbackQuestions(topic);
            return;
        }

        attachAdapter();
    }

    private String cleanOption(String line, String label) {
        if (line == null) {
            return "";
        }

        return line
                .replace(label, "")
                .replace(label.replace(")", "."), "")
                .trim();
    }

    private String cleanAnswer(String line) {
        if (line == null) {
            return "";
        }

        return line
                .replace("Answer:", "")
                .replace("Correct answer:", "")
                .replace("Correct Answer:", "")
                .trim();
    }

    private void loadFallbackQuestions(String topic) {
        questionList.clear();

        questionList.add(new QuizQuestion(
                1,
                "What is an important basic concept in " + topic + "?",
                Arrays.asList("Core rules", "Random guessing", "Ignoring practice", "Unrelated facts"),
                "Core rules"
        ));

        questionList.add(new QuizQuestion(
                2,
                "Why should students learn " + topic + "?",
                Arrays.asList("To build understanding", "To avoid learning", "To skip practice", "To forget basics"),
                "To build understanding"
        ));

        questionList.add(new QuizQuestion(
                3,
                "What is the best way to improve in " + topic + "?",
                Arrays.asList("Regular practice", "No revision", "Avoid examples", "Guessing only"),
                "Regular practice"
        ));

        attachAdapter();
    }

    private void attachAdapter() {
        rvQuestions.setAdapter(new QuestionAdapter(
                questionList,
                (question, tvPrompt, tvResponse, tvError, progressBar) -> {

                    String prompt =
                            "Give one short learning hint for this question without revealing the final answer:\n"
                                    + question.getQuestion();

                    tvPrompt.setText("Prompt:\n" + prompt);
                    tvPrompt.setVisibility(TextView.VISIBLE);

                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    tvResponse.setVisibility(TextView.GONE);
                    tvError.setVisibility(TextView.GONE);

                    generateHintFromLLM(prompt, tvResponse, tvError, progressBar);
                }
        ));

        btnSubmitTask.setEnabled(true);
        btnSubmitTask.setText("Submit Task");
    }

    private void generateHintFromLLM(
            String prompt,
            TextView tvResponse,
            TextView tvError,
            ProgressBar progressBar
    ) {
        if (API_KEY == null || API_KEY.trim().isEmpty()) {
            progressBar.setVisibility(ProgressBar.GONE);
            tvError.setText("API key is missing. Please add it in local.properties.");
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
                progressBar.setVisibility(TextView.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    tvResponse.setText("AI Response:\n" + response.body().getTextResponse());
                    tvResponse.setVisibility(TextView.VISIBLE);
                } else {
                    tvError.setText("Failed to generate hint. Please try again.");
                    tvError.setVisibility(TextView.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                progressBar.setVisibility(TextView.GONE);
                tvError.setText("Network error: " + t.getMessage());
                tvError.setVisibility(TextView.VISIBLE);
            }
        });
    }

    private void submitTask() {
        if (questionList.isEmpty()) {
            Toast.makeText(this, "Questions are still loading. Please wait.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (QuizQuestion question : questionList) {
            if (question.getSelectedAnswer() == null || question.getSelectedAnswer().trim().isEmpty()) {
                Toast.makeText(this, "Please answer all questions before submitting", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        ArrayList<QuizResult> results = new ArrayList<>();
        int score = 0;

        for (QuizQuestion question : questionList) {
            boolean isCorrect = question.getSelectedAnswer().equals(question.getCorrectAnswer());

            if (isCorrect) {
                score++;
            }

            results.add(new QuizResult(
                    question.getQuestion(),
                    question.getSelectedAnswer(),
                    question.getCorrectAnswer(),
                    isCorrect,
                    ""
            ));
        }

        ResultsHolder.results = results;

        Intent intent = new Intent(TaskActivity.this, ResultsActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("total", questionList.size());
        startActivity(intent);
    }

    public static class ResultsHolder {
        public static List<QuizResult> results;
    }
}
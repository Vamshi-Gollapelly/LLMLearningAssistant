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

import com.vamshigollapelly.llmlearningassistant.R;
import com.vamshigollapelly.llmlearningassistant.adapters.QuestionAdapter;
import com.vamshigollapelly.llmlearningassistant.models.QuizQuestion;
import com.vamshigollapelly.llmlearningassistant.models.QuizResult;
import com.vamshigollapelly.llmlearningassistant.utils.DummyData;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private TextView tvTaskTitle, tvTaskDescription;
    private RecyclerView rvQuestions;
    private Button btnSubmitTask;
    private List<QuizQuestion> questionList;

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

        if (taskTitle == null || taskTitle.isEmpty()) {
            taskTitle = "Generated Task 1";
        }

        if (taskDescription == null || taskDescription.isEmpty()) {
            taskDescription = "Small description for the generated task";
        }

        tvTaskTitle.setText(taskTitle);
        tvTaskDescription.setText(taskDescription);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        tvTaskTitle.startAnimation(fadeIn);
        tvTaskDescription.startAnimation(fadeIn);

        questionList = DummyData.getQuizQuestions();

        rvQuestions.setLayoutManager(new LinearLayoutManager(this));
        rvQuestions.setAdapter(new QuestionAdapter(questionList, (question, tvPrompt, tvResponse, tvError, progressBar) -> {
            String prompt = "Give a short learning hint for this question without revealing the answer:\n"
                    + question.getQuestion();

            tvPrompt.setText("Prompt:\n" + prompt);
            tvPrompt.setVisibility(TextView.VISIBLE);

            progressBar.setVisibility(ProgressBar.VISIBLE);
            tvResponse.setVisibility(TextView.GONE);
            tvError.setVisibility(TextView.GONE);

            tvResponse.postDelayed(() -> {
                progressBar.setVisibility(ProgressBar.GONE);
                tvResponse.setText("Hint: Think about the core concept behind this question and eliminate the clearly incorrect options first.");
                tvResponse.setVisibility(TextView.VISIBLE);
            }, 1200);
        }));

        btnSubmitTask.setOnClickListener(v -> {
            ArrayList<String> unanswered = new ArrayList<>();

            for (int i = 0; i < questionList.size(); i++) {
                if (questionList.get(i).getSelectedAnswer() == null || questionList.get(i).getSelectedAnswer().isEmpty()) {
                    unanswered.add("Question " + (i + 1));
                }
            }

            if (!unanswered.isEmpty()) {
                Toast.makeText(this, "Please answer all questions before submitting", Toast.LENGTH_SHORT).show();
                return;
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

            Intent intent = new Intent(TaskActivity.this, ResultsActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("total", questionList.size());
            intent.putParcelableArrayListExtra("resultsList", new ArrayList<>());
            ResultsHolder.results = results;
            startActivity(intent);
        });
    }

    public static class ResultsHolder {
        public static List<QuizResult> results;
    }
}
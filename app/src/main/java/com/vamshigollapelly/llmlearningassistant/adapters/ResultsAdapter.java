package com.vamshigollapelly.llmlearningassistant.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vamshigollapelly.llmlearningassistant.R;
import com.vamshigollapelly.llmlearningassistant.models.QuizResult;

import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultViewHolder> {

    public interface OnExplainClickListener {
        void onExplainClick(
                QuizResult result,
                TextView tvPrompt,
                TextView tvResponse,
                TextView tvError,
                ProgressBar progressBar
        );
    }

    private final List<QuizResult> resultsList;
    private final OnExplainClickListener explainClickListener;

    public ResultsAdapter(List<QuizResult> resultsList, OnExplainClickListener explainClickListener) {
        this.resultsList = resultsList;
        this.explainClickListener = explainClickListener;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_result, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        QuizResult result = resultsList.get(position);

        holder.tvResultQuestion.setText((position + 1) + ". " + result.getQuestion());
        holder.tvSelectedAnswer.setText("Your answer: " + result.getSelectedAnswer());
        holder.tvCorrectAnswer.setText("Correct answer: " + result.getCorrectAnswer());

        if (result.isCorrect()) {
            holder.tvStatus.setText("Correct");
            holder.tvStatus.setTextColor(Color.parseColor("#27F35A"));
        } else {
            holder.tvStatus.setText("Incorrect");
            holder.tvStatus.setTextColor(Color.parseColor("#FF4D4D"));
        }

        holder.itemView.startAnimation(
                AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in)
        );

        holder.btnExplainAnswer.setOnClickListener(v -> {
            if (explainClickListener != null) {
                explainClickListener.onExplainClick(
                        result,
                        holder.tvExplainPrompt,
                        holder.tvExplainResponse,
                        holder.tvExplainError,
                        holder.progressExplain
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    static class ResultViewHolder extends RecyclerView.ViewHolder {

        TextView tvResultQuestion, tvSelectedAnswer, tvCorrectAnswer, tvStatus;
        TextView tvExplainPrompt, tvExplainResponse, tvExplainError;
        Button btnExplainAnswer;
        ProgressBar progressExplain;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);

            tvResultQuestion = itemView.findViewById(R.id.tvResultQuestion);
            tvSelectedAnswer = itemView.findViewById(R.id.tvSelectedAnswer);
            tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
            tvStatus = itemView.findViewById(R.id.tvStatus);

            btnExplainAnswer = itemView.findViewById(R.id.btnExplainAnswer);
            tvExplainPrompt = itemView.findViewById(R.id.tvExplainPrompt);
            tvExplainResponse = itemView.findViewById(R.id.tvExplainResponse);
            tvExplainError = itemView.findViewById(R.id.tvExplainError);
            progressExplain = itemView.findViewById(R.id.progressExplain);
        }
    }
}
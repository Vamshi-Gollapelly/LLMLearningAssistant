package com.vamshigollapelly.llmlearningassistant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vamshigollapelly.llmlearningassistant.R;
import com.vamshigollapelly.llmlearningassistant.models.QuizQuestion;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    public interface OnHintClickListener {
        void onHintClick(
                QuizQuestion question,
                TextView tvPrompt,
                TextView tvResponse,
                TextView tvError,
                ProgressBar progressBar
        );
    }

    private final List<QuizQuestion> questionList;
    private final OnHintClickListener hintClickListener;

    public QuestionAdapter(List<QuizQuestion> questionList, OnHintClickListener hintClickListener) {
        this.questionList = questionList;
        this.hintClickListener = hintClickListener;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        QuizQuestion question = questionList.get(position);

        holder.tvQuestionText.setText((position + 1) + ". " + question.getQuestion());

        List<String> options = question.getOptions();

        holder.rbOption1.setText(options.get(0));
        holder.rbOption2.setText(options.get(1));
        holder.rbOption3.setText(options.get(2));
        holder.rbOption4.setText(options.get(3));

        holder.rgOptions.setOnCheckedChangeListener(null);
        holder.rgOptions.clearCheck();

        String selected = question.getSelectedAnswer();

        if (selected != null && !selected.isEmpty()) {
            if (selected.equals(options.get(0))) {
                holder.rbOption1.setChecked(true);
            } else if (selected.equals(options.get(1))) {
                holder.rbOption2.setChecked(true);
            } else if (selected.equals(options.get(2))) {
                holder.rbOption3.setChecked(true);
            } else if (selected.equals(options.get(3))) {
                holder.rbOption4.setChecked(true);
            }
        }

        holder.rgOptions.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbOption1) {
                question.setSelectedAnswer(holder.rbOption1.getText().toString());
            } else if (checkedId == R.id.rbOption2) {
                question.setSelectedAnswer(holder.rbOption2.getText().toString());
            } else if (checkedId == R.id.rbOption3) {
                question.setSelectedAnswer(holder.rbOption3.getText().toString());
            } else if (checkedId == R.id.rbOption4) {
                question.setSelectedAnswer(holder.rbOption4.getText().toString());
            }
        });

        holder.itemView.startAnimation(
                AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in)
        );

        holder.btnGenerateHint.setOnClickListener(v -> {
            if (hintClickListener != null) {
                hintClickListener.onHintClick(
                        question,
                        holder.tvHintPrompt,
                        holder.tvHintResponse,
                        holder.tvHintError,
                        holder.progressHint
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {

        TextView tvQuestionText, tvHintPrompt, tvHintResponse, tvHintError;
        RadioGroup rgOptions;
        RadioButton rbOption1, rbOption2, rbOption3, rbOption4;
        Button btnGenerateHint;
        ProgressBar progressHint;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);

            tvQuestionText = itemView.findViewById(R.id.tvQuestionText);
            tvHintPrompt = itemView.findViewById(R.id.tvHintPrompt);
            tvHintResponse = itemView.findViewById(R.id.tvHintResponse);
            tvHintError = itemView.findViewById(R.id.tvHintError);

            rgOptions = itemView.findViewById(R.id.rgOptions);
            rbOption1 = itemView.findViewById(R.id.rbOption1);
            rbOption2 = itemView.findViewById(R.id.rbOption2);
            rbOption3 = itemView.findViewById(R.id.rbOption3);
            rbOption4 = itemView.findViewById(R.id.rbOption4);

            btnGenerateHint = itemView.findViewById(R.id.btnGenerateHint);
            progressHint = itemView.findViewById(R.id.progressHint);
        }
    }
}
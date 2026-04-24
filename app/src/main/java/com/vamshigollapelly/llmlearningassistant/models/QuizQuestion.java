package com.vamshigollapelly.llmlearningassistant.models;

import java.util.List;

public class QuizQuestion {

    private int id;
    private String question;
    private List<String> options;
    private String correctAnswer;
    private String selectedAnswer;

    public QuizQuestion(int id, String question, List<String> options, String correctAnswer) {
        this.id = id;
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.selectedAnswer = "";
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }
}
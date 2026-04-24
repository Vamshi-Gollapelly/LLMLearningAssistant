package com.vamshigollapelly.llmlearningassistant.models;

public class QuizResult {

    private String question;
    private String selectedAnswer;
    private String correctAnswer;
    private boolean correct;
    private String explanation;

    public QuizResult(String question, String selectedAnswer, String correctAnswer, boolean correct, String explanation) {
        this.question = question;
        this.selectedAnswer = selectedAnswer;
        this.correctAnswer = correctAnswer;
        this.correct = correct;
        this.explanation = explanation;
    }

    public String getQuestion() {
        return question;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
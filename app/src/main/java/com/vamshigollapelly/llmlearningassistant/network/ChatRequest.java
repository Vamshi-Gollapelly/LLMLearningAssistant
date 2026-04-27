package com.vamshigollapelly.llmlearningassistant.network;

public class ChatRequest {
    private String model;
    private String input;

    public ChatRequest(String model, String input) {
        this.model = model;
        this.input = input;
    }
}
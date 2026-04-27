package com.vamshigollapelly.llmlearningassistant.network;

import java.util.List;

public class ChatResponse {

    private List<Output> output;

    public String getTextResponse() {
        if (output == null || output.isEmpty()) return "No response received.";

        for (Output item : output) {
            if (item.content != null) {
                for (Content content : item.content) {
                    if (content.text != null) {
                        return content.text;
                    }
                }
            }
        }

        return "No text response found.";
    }

    static class Output {
        List<Content> content;
    }

    static class Content {
        String type;
        String text;
    }
}
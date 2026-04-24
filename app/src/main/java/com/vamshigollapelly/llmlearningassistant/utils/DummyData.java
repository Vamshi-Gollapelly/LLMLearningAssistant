package com.vamshigollapelly.llmlearningassistant.utils;

import com.vamshigollapelly.llmlearningassistant.models.QuizQuestion;
import com.vamshigollapelly.llmlearningassistant.models.TaskItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DummyData {

    public static List<String> getTopics() {
        return Arrays.asList(
                "Algorithms",
                "Data Structures",
                "Web Development",
                "Testing",
                "Database",
                "AI Basics",
                "Java",
                "Kotlin",
                "Mobile Development",
                "Cyber Security"
        );
    }

    public static List<TaskItem> getTasks() {
        List<TaskItem> tasks = new ArrayList<>();
        tasks.add(new TaskItem("Generated Task 1", "Small description for the generated task"));
        tasks.add(new TaskItem("Generated Task 2", "Practice task based on your selected interests"));
        return tasks;
    }

    public static List<QuizQuestion> getQuizQuestions() {
        List<QuizQuestion> list = new ArrayList<>();

        list.add(new QuizQuestion(
                1,
                "What is the time complexity of binary search?",
                Arrays.asList("O(n)", "O(log n)", "O(n log n)", "O(1)"),
                "O(log n)"
        ));

        list.add(new QuizQuestion(
                2,
                "Which data structure follows FIFO?",
                Arrays.asList("Stack", "Queue", "Tree", "Graph"),
                "Queue"
        ));

        list.add(new QuizQuestion(
                3,
                "Which HTTP method is mainly used to fetch data?",
                Arrays.asList("POST", "GET", "DELETE", "PUT"),
                "GET"
        ));

        return list;
    }
}
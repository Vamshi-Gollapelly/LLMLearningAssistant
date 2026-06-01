# LLM Learning Assistant

An Android learning app powered by the **OpenAI API (GPT)** that helps students understand concepts through AI-generated hints, explanations, flashcards, and personalised study plans — rather than just giving direct answers.

---

## Screenshots

> Add screenshots after running the app on an emulator

| Login | Interests | Home | Task | Results | Flashcards | Study Plan |
|---|---|---|---|---|---|---|
| *(screenshot)* | *(screenshot)* | *(screenshot)* | *(screenshot)* | *(screenshot)* | *(screenshot)* | *(screenshot)* |

---

## Features

- **AI-generated learning tasks** — generates questions based on the user's selected interests via OpenAI API
- **Hint system** — asks GPT for a hint rather than the answer, encouraging active problem-solving
- **Answer explanation** — GPT explains why an answer is correct or incorrect after submission
- **Flashcard generation** — automatically creates revision flashcards for the current topic
- **7-day study plan** — generates a personalised study schedule based on selected interests
- **User authentication** — login and registration with local persistence
- **Loading & error states** — proper handling of API latency and network errors
- **Clean, minimal UI** — designed for focus and readability

---

## Tech stack

![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=flat&logo=android&logoColor=white)
![OpenAI](https://img.shields.io/badge/OpenAI%20API-412991?style=flat&logo=openai&logoColor=white)

| Layer | Technology |
|---|---|
| Language | Java |
| AI / LLM | OpenAI API (GPT) |
| HTTP client | Retrofit / OkHttp (for API calls) |
| UI | XML layouts, multiple Activities |
| Build | Gradle |

---

## Architecture

```
app/
├── api/
│   ├── OpenAIClient.java        # Retrofit instance — OpenAI base URL
│   ├── OpenAIService.java       # API interface — /v1/chat/completions
│   └── OpenAIRequest.java       # Request body model (model, messages, max_tokens)
├── model/
│   ├── User.java
│   └── Message.java             # Chat message model (role + content)
├── prompts/
│   └── PromptBuilder.java       # Builds structured prompts per feature
└── ui/
    ├── LoginActivity.java
    ├── RegisterActivity.java
    ├── InterestsActivity.java    # User selects learning topics
    ├── HomeActivity.java
    ├── TaskActivity.java         # Displays AI-generated question
    ├── ResultsActivity.java      # Shows answer + GPT explanation
    ├── FlashcardsActivity.java   # AI-generated flashcard deck
    └── StudyPlanActivity.java    # 7-day AI-generated study schedule
```

---

## How the OpenAI integration works

Each feature sends a structured prompt to the **OpenAI Chat Completions API** (`/v1/chat/completions`) and parses the response:

```java
// Example — generating a hint
String prompt = "The student is answering this question: '" + question + "'. " +
                "Give a helpful hint that guides them toward the answer " +
                "without revealing it directly. Keep it under 3 sentences.";

OpenAIRequest request = new OpenAIRequest(
    "gpt-3.5-turbo",
    Collections.singletonList(new Message("user", prompt)),
    150
);

openAIService.getCompletion(request).enqueue(new Callback<OpenAIResponse>() {
    @Override
    public void onResponse(Call<OpenAIResponse> call, Response<OpenAIResponse> response) {
        String hint = response.body().getChoices().get(0).getMessage().getContent();
        hintTextView.setText(hint);
    }
});
```

**Prompt strategies used per feature:**

| Feature | Prompt approach |
|---|---|
| Learning task | "Generate a [topic] question for a student at beginner level" |
| Hint | "Give a hint without revealing the answer directly" |
| Explanation | "Explain why [answer] is correct/incorrect for this question" |
| Flashcards | "Generate 5 flashcard pairs (term + definition) for [topic]" |
| Study plan | "Create a 7-day study plan for someone learning [topics]" |

---

## Getting started

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 26+
- Java 11+
- An OpenAI API key ([get one here](https://platform.openai.com/api-keys))

### Run locally

```bash
git clone https://github.com/Vamshi-Gollapelly/LLMLearningAssistant.git
```

1. Open in Android Studio
2. Add your OpenAI API key to `local.properties`:
   ```
   OPENAI_API_KEY=sk-your-key-here
   ```
3. Let Gradle sync
4. Run on emulator or physical device (API 26+)

> ⚠️ Never commit your API key directly into source code. Use `local.properties` or environment variables.

---

## Planned improvements

- [ ] Switch from GPT-3.5 to GPT-4o for richer explanations
- [ ] Cache flashcards and study plans locally with Room Database
- [ ] Add spaced repetition algorithm to flashcard review
- [ ] Track learning progress over time with a dashboard
- [ ] Support voice input for questions

---

## What I learned

- Integrating a live REST API (OpenAI) into an Android app using Retrofit
- Designing effective prompts for different learning use cases
- Handling asynchronous API calls with proper loading and error states
- Structuring prompt engineering logic separately from UI code
- Building a multi-screen Android app with a coherent user journey

---

## Author

**Vamshi Gollapelly**
[LinkedIn](https://linkedin.com/in/vamshigollapelly) · [GitHub](https://github.com/Vamshi-Gollapelly) · [Email](mailto:vamshigollapelly225@gmail.com)

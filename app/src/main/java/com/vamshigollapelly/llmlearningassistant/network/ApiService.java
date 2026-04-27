package com.vamshigollapelly.llmlearningassistant.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    @POST("v1/responses")
    Call<ChatResponse> getLearningResponse(
            @Header("Authorization") String authorization,
            @Header("Content-Type") String contentType,
            @Body ChatRequest request
    );
}
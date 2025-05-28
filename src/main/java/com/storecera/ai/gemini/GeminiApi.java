package com.storecera.ai.gemini;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Headers;

public interface GeminiApi {

    @Headers("Content-Type: application/json")
//    @POST("v1beta/models/gemini-pro:generateContent")
//    @POST("models/gemini-pro:generateContent")
//    @POST("models/gemini-2.0-flash:generateContent")
    @POST("models/gemini-1.5-pro:generateContent")
    Call<GeminiResponse> generateContent(
            @Query("key") String apiKey,
            @Body GeminiRequest request
    );
}

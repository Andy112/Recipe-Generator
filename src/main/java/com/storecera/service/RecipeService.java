package com.storecera.service;

import com.storecera.ai.gemini.GeminiClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class RecipeService {

    private final GeminiClient geminiClient;

    public RecipeService(GeminiClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    public CompletableFuture<String> generateResponse(String prompt) {
        CompletableFuture<String> future = new CompletableFuture<>();

//        geminiClient.generateContent(prompt, new GeminiClient.GeminiCallback() {
//            @Override
//            public void onSuccess(String response) {
//                future.complete(response);
//            }
//
//            @Override
//            public void onError(String error) {
//                future.completeExceptionally(new RuntimeException(error));
//            }
//        });

        return geminiClient.generateContent(prompt);
    }
}

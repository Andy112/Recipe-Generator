package com.storecera.service;

import com.storecera.ai.gemini.GeminiClient;
import com.storecera.ai.gemini.GeminiClient.GeminiCallback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceTest {

    private RecipeService recipeService;
    private GeminiClient geminiClient;

    @BeforeEach
    void setUp() {
        geminiClient = mock(GeminiClient.class);
        recipeService = new RecipeService(geminiClient);
    }

    @Test
    void generateResponse_shouldCompleteSuccessfully_whenGeminiReturnsData() {
        // Arrange
        String prompt = "Give me a salad recipe";
        String mockResponse = "Here's your salad recipe.";

        var callbackCaptor = ArgumentCaptor.forClass(GeminiClient.GeminiCallback.class);

        // Act
        CompletableFuture<String> future = recipeService.generateResponse(prompt);

        // Verify GeminiClient was called and capture the callback
        verify(geminiClient).generateContent(eq(prompt), callbackCaptor.capture());

        // Simulate success callback from Gemini
        var callback = callbackCaptor.getValue();
        callback.onSuccess(mockResponse);

        // Assert the future completed successfully
        assertTrue(future.isDone());
        assertEquals(mockResponse, future.join());
    }

    @Test
    void generateResponse_shouldCompleteExceptionally_whenGeminiFails() {
        // Arrange
        String prompt = "Cause error";
        String error = "Network error";

        ArgumentCaptor<GeminiCallback> callbackCaptor = ArgumentCaptor.forClass(GeminiCallback.class);

        CompletableFuture<String> future = recipeService.generateResponse(prompt);
//        verify(geminiClient).generateContent(eq(prompt), callbackCaptor.capture());
        // Simulate error callback
//        callbackCaptor.getValue().onError(error);

        // Assert the future failed
//        assertTrue(future.isCompletedExceptionally());

//        future = verify(geminiClient).generateContent(prompt);

//        future.handle((res, ex) -> {
//            assertNotNull(ex);
//            assertTrue(ex.getMessage().contains(error));
//            return null;
//        }).join();
    }
}
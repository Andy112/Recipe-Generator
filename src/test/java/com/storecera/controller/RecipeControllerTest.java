package com.storecera.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storecera.dto.PromptRequest;
import com.storecera.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test for RecipeController that checks the end-to-end flow
 *It sends a POST request with a prompt
 *It mocks the Gemini client to simulate a successful response
 * It verifies that the JSON returned is what you expect
 */
@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecipeService recipeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        // setup logic if needed
    }

    @Test
    void testHandlePrompt_ReturnsValidResponse() throws Exception {
        // Arrange
        String prompt = "What is AI?";
        String fakeResponse = "Artificial Intelligence is the simulation of human intelligence.";

        Mockito.when(recipeService.generateResponse(anyString()))
                .thenReturn(CompletableFuture.completedFuture(fakeResponse));

        PromptRequest request = new PromptRequest();
        request.setPrompt(prompt);

        // Act & Assert
//        mockMvc.perform(post("/api/vi/recipes")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.result").value(fakeResponse));

        var mvcResult = mockMvc.perform(post("/api/vi/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(request().asyncStarted())  // ✅ Assert async starts
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))  // ✅ Wait for async to finish
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))  // Optional
                .andExpect(jsonPath("$.result").value(fakeResponse))
                .andDo(print());
    }
}


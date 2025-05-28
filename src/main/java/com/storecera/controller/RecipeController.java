package com.storecera.controller;

import com.storecera.dto.PromptRequest;
import com.storecera.dto.PromptResponse;
import com.storecera.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/vi/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<PromptResponse>> handlePrompt(@RequestBody PromptRequest request) {
        return recipeService.generateResponse(request.getPrompt())
                .thenApply(response -> ResponseEntity
                        .ok(new PromptResponse(response)));
    }
}

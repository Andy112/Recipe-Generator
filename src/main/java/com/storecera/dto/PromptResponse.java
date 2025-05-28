package com.storecera.dto;

public class PromptResponse {
    private String result;

    public PromptResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {  // Optional, but useful
        this.result = result;
    }
}

package com.storecera.ai.gemini;

import java.util.List;

public class GeminiRequest {
    public List<Content> contents;

    public GeminiRequest(String input) {
        this.contents = List.of(new Content(new Part(input)));
    }

    public static class Content {
        public List<Part> parts;
        public String role = "user";

        public Content(Part part) {
            this.parts = List.of(part);
        }
    }

    public static class Part {
        public String text;

        public Part(String text) {
            this.text = text;
        }
    }
}

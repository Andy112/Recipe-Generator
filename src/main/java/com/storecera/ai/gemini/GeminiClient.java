package com.storecera.ai.gemini;

import com.storecera.util.GsonSingleton;
import com.storecera.util.L;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GeminiClient {

    private static final String API_KEY = "AIzaSyBZ2rhCjwsceBWjWz3uKaAuBlJq_6s-1qo";
    private static final String ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key="
            + API_KEY;

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    public interface GeminiCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public void generateContent(String prompt, GeminiCallback callback) {
        L.msg("Prompt: " + prompt);
        try {
            JSONObject json = new JSONObject();
            JSONArray parts = new JSONArray();
            JSONObject part = new JSONObject();
            part.put("text", prompt);
            parts.put(part);

            JSONObject request = new JSONObject();
            request.put("contents", new JSONObject().put("parts", parts));

            RequestBody body = RequestBody.create(MediaType.parse("application/json"),
                    request.toString());

            Request requestHttp = new Request.Builder()
                    .url(ENDPOINT)
                    .post(body)
                    .build();

            client.newCall(requestHttp).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onError("Gemini API Error: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseBody = response.body().string();
                            L.msg("ResponseBody: " + responseBody);
                            GeminiResponse gr = GsonSingleton.get().fromJson(responseBody, GeminiResponse.class);
                            String reply = gr.candidates.get(0).content.parts.get(0).text;
                            callback.onSuccess(reply);
                        } catch (Exception e) {
                            callback.onError("Gemini API Failed: " + e.getLocalizedMessage());
                        }
                    } else {
                        callback.onError("Gemini API Failed: " + response.code());
                    }
                }
            });

        } catch (Exception e) {
            callback.onError("Gemini Error: " + e.getMessage());
        }
    }
}

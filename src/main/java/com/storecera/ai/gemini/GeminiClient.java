package com.storecera.ai.gemini;

import com.storecera.util.L;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class GeminiClient {

//    https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent
    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/";
    private static Retrofit retrofit;

    public static GeminiApi getGeminiApi() {
        if (retrofit == null) {
//            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
//                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS);

            OkHttpClient client = httpClient.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit.create(GeminiApi.class);
    }

    public void callGemini(String userInput, Consumer<String> listener) {
        String apiKey = "AIzaSyBZ2rhCjwsceBWjWz3uKaAuBlJq_6s-1qo"; // Use secured way in production
        GeminiRequest request = new GeminiRequest(userInput);
        GeminiApi api = GeminiClient.getGeminiApi();

        api.generateContent(apiKey, request)
                .enqueue(new retrofit2.Callback<GeminiResponse>() {
            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String reply = response.body().candidates.get(0).content.parts.get(0).text;
                    L.msg("Gemini says: " + reply);
                    listener.accept(reply);
                    // Add to UI / chat thread
                } else {
                    L.msg("Gemini API Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                L.msg("Gemini Api Error: " + t.getLocalizedMessage());
                t.printStackTrace();
            }
        });
    }

}

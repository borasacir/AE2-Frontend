package com.sabanciuniv.appliedenergetics2;

import okhttp3.*;
import com.google.gson.Gson;
import java.io.IOException;

public class ApiClient {

    private static final String API_BASE_URL = "http://10.0.2.2:8080/screen";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    public static void loginUser(String username, String password, Callback callback) {
        User user = new User(username, password);
        String requestBody = gson.toJson(user);

        Request request = new Request.Builder()
                .url(API_BASE_URL + "/login")
                .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void registerUser(String username, String password, Callback callback) {
        User user = new User(username, password);
        String requestBody = gson.toJson(user);

        Request request = new Request.Builder()
                .url(API_BASE_URL + "/signup")
                .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                .build();

        client.newCall(request).enqueue(callback);
    }
}

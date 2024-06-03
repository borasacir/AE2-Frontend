package com.sabanciuniv.appliedenergetics2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements ModpackAdapter.OnModpackClickListener {

    private RecyclerView recyclerView;
    private ModpackAdapter adapter;
    private OkHttpClient client;
    private static final String BASE_URL = "http://10.0.2.2:8080/api/";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        ImageButton btnSearch = findViewById(R.id.btn_search);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        client = new OkHttpClient();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement search functionality
                searchModpacks("search_query"); // Replace with actual search query
            }
        });

        fetchModpacks();
    }

    private void fetchModpacks() {
        Request request = new Request.Builder()
                .url(BASE_URL + "index")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error fetching modpacks", e);
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error fetching modpacks", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to load modpacks", Toast.LENGTH_SHORT).show());
                    return;
                }

                String responseBody = response.body().string();
                Gson gson = new Gson();
                Type modpackListType = new TypeToken<List<Modpack>>() {}.getType();
                List<Modpack> modpacks = gson.fromJson(responseBody, modpackListType);

                runOnUiThread(() -> {
                    if (!modpacks.isEmpty()) {
                        adapter = new ModpackAdapter(modpacks.get(0).getItems(), MainActivity.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(MainActivity.this, "No modpacks available", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void searchModpacks(String query) {
        Request request = new Request.Builder()
                .url(BASE_URL + "index/search?query=" + query)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error searching modpacks", e);
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error searching modpacks", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "No results found", Toast.LENGTH_SHORT).show());
                    return;
                }

                String responseBody = response.body().string();
                Gson gson = new Gson();
                Type modpackListType = new TypeToken<List<Modpack>>() {}.getType();
                List<Modpack> modpacks = gson.fromJson(responseBody, modpackListType);

                runOnUiThread(() -> {
                    if (!modpacks.isEmpty()) {
                        adapter.setItemList(modpacks.get(0).getItems());
                    } else {
                        Toast.makeText(MainActivity.this, "No modpacks available", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onModpackClick(String itemId) {
        Intent intent = new Intent(this, ModpackDetailActivity.class);
        intent.putExtra("itemId", itemId);
        startActivity(intent);
    }
}

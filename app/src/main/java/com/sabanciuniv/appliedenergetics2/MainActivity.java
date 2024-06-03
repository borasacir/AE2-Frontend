package com.sabanciuniv.appliedenergetics2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements ModpackAdapter.OnModpackClickListener {

    private RecyclerView recyclerView;
    private ModpackAdapter adapter;
    private OkHttpClient client;
    private SearchView searchView;
    private List<Modpack.Item> allItems;
    private static final String BASE_URL = "http://10.0.2.2:8080/api/";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.search_view);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        client = new OkHttpClient();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    searchModpacks(query);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a search term", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    fetchModpacks();
                } else {
                    searchModpacks(newText);
                }
                return false;
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
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to load modpacks", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    String responseBodyString = responseBody.string();
                    Log.d(TAG, "JSON Response: " + responseBodyString);
                    Gson gson = new Gson();
                    Type modpackListType = new TypeToken<List<Modpack>>() {}.getType();
                    List<Modpack> modpacks = gson.fromJson(responseBodyString, modpackListType);

                    runOnUiThread(() -> {
                        if (!modpacks.isEmpty()) {
                            allItems = modpacks.stream()
                                    .flatMap(modpack -> modpack.getItems().stream())
                                    .collect(Collectors.toList());

                            adapter = new ModpackAdapter(allItems, MainActivity.this);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(MainActivity.this, "No modpacks available", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void searchModpacks(String query) {
        Request request = new Request.Builder()
                .url(BASE_URL + "index/search?title=" + query)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error searching modpacks", e);
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error searching modpacks", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        Log.d("anan", "Searched: " + query);
                        Log.d("anan2", "Sex: "+ response);
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "No results found", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    String responseBodyString = responseBody.string();
                    Gson gson = new Gson();
                    Type itemListType = new TypeToken<List<Modpack.Item>>() {}.getType();
                    List<Modpack.Item> items = gson.fromJson(responseBodyString, itemListType);

                    runOnUiThread(() -> {
                        if (items != null && !items.isEmpty()) {
                            adapter.setItemList(items);
                        } else {
                            Toast.makeText(MainActivity.this, "No items found", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onModpackClick(String itemTitle) {
        Log.d(TAG, "Clicked item title: " + itemTitle);
        Intent intent = new Intent(this, ModpackDetailActivity.class);
        intent.putExtra("title", itemTitle);
        startActivity(intent);
    }
}

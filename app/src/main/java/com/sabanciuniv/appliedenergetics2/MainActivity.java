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
import com.sabanciuniv.appliedenergetics2.Modpack;
import com.sabanciuniv.appliedenergetics2.ModpackAdapter;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements ModpackAdapter.OnModpackClickListener {

    private RecyclerView recyclerView;
    private ModpackAdapter adapter;
    private ApiService apiService;

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

        Retrofit retrofit = RetrofitClient.getClient("http://10.0.2.2:8080/api/");

        apiService = retrofit.create(ApiService.class);

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
        Call<List<Modpack>> call = apiService.getModpacks();
        call.enqueue(new Callback<List<Modpack>>() {
            @Override
            public void onResponse(Call<List<Modpack>> call, Response<List<Modpack>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Modpack> modpacks = response.body();
                    adapter = new ModpackAdapter(modpacks, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load modpacks", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Modpack>> call, Throwable t) {
                Log.e("MainActivity", "Error fetching modpacks", t);
                Toast.makeText(MainActivity.this, "Error fetching modpacks", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchModpacks(String query) {
        Call<List<Modpack>> call = apiService.searchModpacks(query);
        call.enqueue(new Callback<List<Modpack>>() {
            @Override
            public void onResponse(Call<List<Modpack>> call, Response<List<Modpack>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Modpack> modpacks = response.body();
                    adapter.setModpackList(modpacks);
                } else {
                    Toast.makeText(MainActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Modpack>> call, Throwable t) {
                Log.e("MainActivity", "Error searching modpacks", t);
                Toast.makeText(MainActivity.this, "Error searching modpacks", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onModpackClick(int modpackId) {
        Intent intent = new Intent(this, ModpackDetailActivity.class);
        intent.putExtra("modpackId", modpackId);
        startActivity(intent);
    }
}

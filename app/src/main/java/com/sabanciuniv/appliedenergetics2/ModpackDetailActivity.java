package com.sabanciuniv.appliedenergetics2;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class ModpackDetailActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://10.0.2.2:8080/api/";
    private static final String TAG = "ModpackDetailActivity";

    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView imageView;
    private TextView recipeUrlTextView;

    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modpack_detail);

        titleTextView = findViewById(R.id.modpack_title);
        descriptionTextView = findViewById(R.id.modpack_description);
        imageView = findViewById(R.id.modpack_image);
        recipeUrlTextView = findViewById(R.id.modpack_recipe_url);

        client = new OkHttpClient();

        String itemId = getIntent().getStringExtra("itemId");
        if (itemId != null) {
            fetchModpackItemDetails(itemId);
        } else {
            Toast.makeText(this, "No item ID found", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchModpackItemDetails(String itemId) {
        Request request = new Request.Builder()
                .url(BASE_URL + "modpackitem/" + itemId)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error fetching modpack item details", e);
                runOnUiThread(() -> Toast.makeText(ModpackDetailActivity.this, "Error fetching modpack item details", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(ModpackDetailActivity.this, "Failed to load modpack item details", Toast.LENGTH_SHORT).show());
                    return;
                }

                String responseBody = response.body().string();
                Gson gson = new Gson();
                Modpack.Item item = gson.fromJson(responseBody, Modpack.Item.class);

                runOnUiThread(() -> {
                    titleTextView.setText(item.getTitle());
                    descriptionTextView.setText(item.getDescription());
                    Glide.with(ModpackDetailActivity.this).load(item.getImageURL()).into(imageView);
                    recipeUrlTextView.setText(item.getRecipeURL());
                });
            }
        });
    }
}

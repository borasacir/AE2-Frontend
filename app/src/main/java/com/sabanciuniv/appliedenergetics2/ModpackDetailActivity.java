package com.sabanciuniv.appliedenergetics2;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.sabanciuniv.appliedenergetics2.Modpack;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ModpackDetailActivity extends AppCompatActivity {

    private ApiService apiService;
    private TextView modpackName;
    private TextView modpackVersion;
    private TextView modpackDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modpack_detail);

        modpackName = findViewById(R.id.modpack_name);
        modpackVersion = findViewById(R.id.modpack_version);
        modpackDescription = findViewById(R.id.modpack_description);

        int modpackId = getIntent().getIntExtra("modpackId", -1);

        Retrofit retrofit = RetrofitClient.getClient("http://10.0.2.2:8080");
        apiService = retrofit.create(ApiService.class);

        fetchModpackDetails(modpackId);
    }

    private void fetchModpackDetails(int modpackId) {
        Call<Modpack> call = apiService.getModpackDetails(modpackId);
        call.enqueue(new Callback<Modpack>() {
            @Override
            public void onResponse(Call<Modpack> call, Response<Modpack> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Modpack modpack = response.body();
                    modpackName.setText(modpack.getName());
                    modpackVersion.setText(modpack.getVersion());
                    modpackDescription.setText(modpack.getDescription());
                } else {
                    Toast.makeText(ModpackDetailActivity.this, "Failed to load modpack details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Modpack> call, Throwable t) {
                Log.e("ModpackDetailActivity", "Error fetching modpack details", t);
                Toast.makeText(ModpackDetailActivity.this, "Error fetching modpack details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

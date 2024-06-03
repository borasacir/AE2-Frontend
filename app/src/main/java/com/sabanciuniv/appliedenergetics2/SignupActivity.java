package com.sabanciuniv.appliedenergetics2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import java.io.IOException;

public class SignupActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etConfirmPassword;
    private Button btnSignup;
    private ProgressBar loadingPB;
    private static final String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnSignup = findViewById(R.id.btn_signup);
        loadingPB = findViewById(R.id.loadingPB);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                performSignup(username, password);
            }
        });
    }

    private void performSignup(String username, String password) {
        loadingPB.setVisibility(View.VISIBLE);

        ApiClient.registerUser(username, password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Signup failed", e);
                runOnUiThread(() -> {
                    loadingPB.setVisibility(View.GONE);
                    Toast.makeText(SignupActivity.this, "Signup failed. Please try again.", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> loadingPB.setVisibility(View.GONE));
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d(TAG, "Response: " + responseBody);

                    if ("User registered!".equals(responseBody)) {
                        Toast.makeText(SignupActivity.this, "Signup successful! Please log in.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        runOnUiThread(() -> Toast.makeText(SignupActivity.this, responseBody, Toast.LENGTH_SHORT).show());
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(SignupActivity.this, "Signup failed. Please try again.", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}

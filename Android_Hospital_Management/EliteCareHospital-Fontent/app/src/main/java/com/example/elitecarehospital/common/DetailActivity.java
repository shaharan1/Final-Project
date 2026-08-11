package com.example.elitecarehospital.common;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.elitecarehospital.R;
import com.example.elitecarehospital.network.ApiClient;
import com.example.elitecarehospital.network.ApiClientError;
import com.example.elitecarehospital.network.ApiService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Generic key-value detail screen.
 * Receives a raw JSON object (EXTRA_JSON) or fetches from an endpoint (EXTRA_ENDPOINT).
 */
public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_JSON = "extra_json";
    public static final String EXTRA_ENDPOINT = "extra_endpoint";
    public static final String EXTRA_TITLE = "extra_title";

    private LinearLayout container;
    private ProgressBar progressBar;
    private TextView tvError;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        container = findViewById(R.id.detailContainer);
        progressBar = findViewById(R.id.progressBar);
        tvError = findViewById(R.id.tvError);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (title != null) getSupportActionBar().setTitle(title);

        String json = getIntent().getStringExtra(EXTRA_JSON);
        if (json != null) {
            try {
                render(com.google.gson.JsonParser.parseString(json).getAsJsonObject());
            } catch (Exception e) {
                showError("Invalid data: " + e.getMessage());
            }
        } else {
            fetchFromEndpoint();
        }
    }

    private void fetchFromEndpoint() {
        String endpoint = getIntent().getStringExtra(EXTRA_ENDPOINT);
        if (endpoint == null) {
            showError("No data provided.");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        ApiClient.getInstance(this).get(endpoint).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null
                        && response.body().isJsonObject()) {
                    render(response.body().getAsJsonObject());
                } else {
                    showError("Failed: " + ApiClientError.getMessage(response));
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showError(ApiClientError.getMessage(t));
                Toast.makeText(DetailActivity.this, ApiClientError.getMessage(t), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void render(JsonObject obj) {
        container.removeAllViews();
        Map<String, String> flat = JsonUtil.flatten(obj);
        if (flat.isEmpty()) {
            showError("No data.");
            return;
        }
        for (Map.Entry<String, String> e : flat.entrySet()) {
            View row = getLayoutInflater().inflate(R.layout.item_detail_row, container, false);
            TextView tvKey = row.findViewById(R.id.tvKey);
            TextView tvValue = row.findViewById(R.id.tvValue);
            tvKey.setText(e.getKey());
            tvValue.setText(e.getValue());
            container.addView(row);
        }
        container.setVisibility(View.VISIBLE);
    }

    private void showError(String msg) {
        tvError.setText(msg);
        tvError.setVisibility(View.VISIBLE);
    }
}

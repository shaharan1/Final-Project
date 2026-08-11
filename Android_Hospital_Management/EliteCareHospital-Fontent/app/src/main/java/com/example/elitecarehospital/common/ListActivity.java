package com.example.elitecarehospital.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.elitecarehospital.R;
import com.example.elitecarehospital.network.ApiClient;
import com.example.elitecarehospital.network.ApiClientError;
import com.example.elitecarehospital.network.ApiService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Generic data-driven list screen.
 *
 * Extras:
 *  EXTRA_TITLE          – toolbar title
 *  EXTRA_ENDPOINT       – relative API path, e.g. "api/patients"
 *  EXTRA_QUERY_JSON     – optional "{\"key\":\"value\"}" query params
 *  EXTRA_TITLE_FIELDS   – JSON array of field names shown as row title
 *  EXTRA_SUBTITLE_FIELDS– JSON array of field names shown as row subtitle
 */
public class ListActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_ENDPOINT = "extra_endpoint";
    public static final String EXTRA_QUERY_JSON = "extra_query_json";
    public static final String EXTRA_TITLE_FIELDS = "extra_title_fields";
    public static final String EXTRA_SUBTITLE_FIELDS = "extra_subtitle_fields";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private EditText etSearch;

    private final List<JsonObject> rows = new ArrayList<>();
    private final List<JsonObject> filtered = new ArrayList<>();
    private RowAdapter adapter;

    private String endpoint;
    private String queryJson;
    private List<String> titleFields;
    private List<String> subtitleFields;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);
        etSearch = findViewById(R.id.etSearch);
        ImageButton btnClear = findViewById(R.id.btnClear);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        endpoint = getIntent().getStringExtra(EXTRA_ENDPOINT);
        queryJson = getIntent().getStringExtra(EXTRA_QUERY_JSON);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (title != null) getSupportActionBar().setTitle(title);

        titleFields = JsonUtil.getFields(new JsonObject(), getIntent().getStringExtra(EXTRA_TITLE_FIELDS));
        subtitleFields = JsonUtil.getFields(new JsonObject(), getIntent().getStringExtra(EXTRA_SUBTITLE_FIELDS));

        adapter = new RowAdapter(filtered, titleFields, subtitleFields, (obj, position) -> {
            Intent i = new Intent(this, DetailActivity.class);
            i.putExtra(DetailActivity.EXTRA_JSON, obj.toString());
            i.putExtra(DetailActivity.EXTRA_TITLE, JsonUtil.joinValues(obj, titleFields, " - "));
            startActivity(i);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        swipeRefresh.setOnRefreshListener(this::fetchData);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) { applyFilter(s.toString()); }
        });
        btnClear.setOnClickListener(v -> {
            etSearch.setText("");
            applyFilter("");
        });

        fetchData();
    }

    private void fetchData() {
        progressBar.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);

        ApiService api = ApiClient.getInstance(this);
        Call<JsonElement> call;
        if (queryJson != null && !queryJson.isEmpty()) {
            Map<String, String> params = new HashMap<>();
            try {
                JsonObject q = JsonParser.parseString(queryJson).getAsJsonObject();
                for (Map.Entry<String, JsonElement> e : q.entrySet()) {
                    params.put(e.getKey(), e.getValue().getAsString());
                }
            } catch (Exception ignored) {
            }
            call = api.getWithParams(endpoint, params);
        } else {
            call = api.get(endpoint);
        }

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                swipeRefresh.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                if (!response.isSuccessful() || response.body() == null) {
                    tvEmpty.setText("Failed: " + ApiClientError.getMessage(response));
                    tvEmpty.setVisibility(View.VISIBLE);
                    return;
                }
                rows.clear();
                JsonElement body = response.body();
                if (body.isJsonArray()) {
                    for (JsonElement el : body.getAsJsonArray()) {
                        if (el.isJsonObject()) rows.add(el.getAsJsonObject());
                    }
                } else if (body.isJsonObject()) {
                    rows.add(body.getAsJsonObject());
                }
                applyFilter(etSearch.getText().toString());
                if (rows.isEmpty()) {
                    tvEmpty.setText("No records found.");
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                tvEmpty.setText(ApiClientError.getMessage(t));
                tvEmpty.setVisibility(View.VISIBLE);
                Toast.makeText(ListActivity.this, ApiClientError.getMessage(t), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void applyFilter(String query) {
        filtered.clear();
        for (JsonObject obj : rows) {
            if (TextUtils.isEmpty(query)) {
                filtered.add(obj);
            } else {
                String haystack = obj.toString().toLowerCase();
                if (haystack.contains(query.toLowerCase())) {
                    filtered.add(obj);
                }
            }
        }
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }

    static class RowAdapter extends RecyclerView.Adapter<RowAdapter.VH> {

        interface OnRowClick {
            void onClick(JsonObject obj, int position);
        }

        private final List<JsonObject> data;
        private final List<String> titleFields;
        private final List<String> subtitleFields;
        private final OnRowClick listener;

        RowAdapter(List<JsonObject> data, List<String> titleFields,
                   List<String> subtitleFields, OnRowClick listener) {
            this.data = data;
            this.titleFields = titleFields;
            this.subtitleFields = subtitleFields;
            this.listener = listener;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            JsonObject obj = data.get(position);
            String title = JsonUtil.joinValues(obj, titleFields, " - ");
            holder.title.setText(TextUtils.isEmpty(title) ? "Record #" + (position + 1) : title);
            String sub = JsonUtil.joinValues(obj, subtitleFields, "  •  ");
            holder.subtitle.setVisibility(TextUtils.isEmpty(sub) ? View.GONE : View.VISIBLE);
            holder.subtitle.setText(sub);
            holder.itemView.setOnClickListener(v -> listener.onClick(obj, holder.getBindingAdapterPosition()));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class VH extends RecyclerView.ViewHolder {
            TextView title, subtitle;

            VH(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.tvRowTitle);
                subtitle = itemView.findViewById(R.id.tvRowSubtitle);
            }
        }
    }
}

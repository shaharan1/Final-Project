package com.example.elitecarehospital;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.elitecarehospital.network.ApiClient;
import com.example.elitecarehospital.network.ApiClientError;
import com.example.elitecarehospital.network.ApiService;
import com.example.elitecarehospital.utils.SessionManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private SessionManager session;
    private TextView tvName, tvEmail, tvRole, tvPhone;
    private ProgressBar progressBar;
    private EditText etName, etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        session = new SessionManager(this);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvRole = findViewById(R.id.tvRole);
        tvPhone = findViewById(R.id.tvPhone);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        progressBar = findViewById(R.id.progressBar);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnChangePassword = findViewById(R.id.btnChangePassword);
        Button btnLogout = findViewById(R.id.btnLogout);

        loadProfile();

        btnSave.setOnClickListener(v -> saveProfile());
        btnChangePassword.setOnClickListener(v -> showPasswordDialog());
        btnLogout.setOnClickListener(v -> {
            session.clear();
            ApiClient.reset();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void loadProfile() {
        progressBar.setVisibility(View.VISIBLE);
        ApiClient.getInstance(this).getProfile().enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().isJsonObject()) {
                    JsonObject obj = response.body().getAsJsonObject();
                    String name = getString(obj, "name");
                    String email = getString(obj, "email");
                    String phone = getString(obj, "phone");
                    String role = getString(obj, "role");
                    tvName.setText(name);
                    tvEmail.setText(email);
                    tvRole.setText(role);
                    tvPhone.setText(phone);
                    etName.setText(name);
                    etPhone.setText(phone);
                } else {
                    tvName.setText(session.getName());
                    tvEmail.setText(session.getEmail());
                    tvRole.setText(session.getRole());
                    tvPhone.setText(session.getPhone());
                    etName.setText(session.getName());
                    etPhone.setText(session.getPhone());
                    Toast.makeText(ProfileActivity.this, ApiClientError.getMessage(response), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                tvName.setText(session.getName());
                tvEmail.setText(session.getEmail());
                tvRole.setText(session.getRole());
                tvPhone.setText(session.getPhone());
                etName.setText(session.getName());
                etPhone.setText(session.getPhone());
                Toast.makeText(ProfileActivity.this, ApiClientError.getMessage(t), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveProfile() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        if (name.isEmpty()) {
            etName.setError("Name required");
            return;
        }

        JsonObject body = new JsonObject();
        body.addProperty("name", name);
        body.addProperty("phone", phone);

        progressBar.setVisibility(View.VISIBLE);
        ApiClient.getInstance(this).updateProfile(body).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    session.updateProfile(name, phone);
                    tvName.setText(name);
                    tvPhone.setText(phone);
                    Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, ApiClientError.getMessage(response), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProfileActivity.this, ApiClientError.getMessage(t), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showPasswordDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        EditText etCurrent = view.findViewById(R.id.etCurrentPassword);
        EditText etNew = view.findViewById(R.id.etNewPassword);

        new AlertDialog.Builder(this)
                .setTitle("Change Password")
                .setView(view)
                .setPositiveButton("Change", (d, w) -> {
                    String current = etCurrent.getText().toString().trim();
                    String newPass = etNew.getText().toString().trim();
                    if (current.isEmpty() || newPass.isEmpty()) {
                        Toast.makeText(this, "Fill both fields", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    changePassword(current, newPass);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void changePassword(String current, String newPass) {
        JsonObject body = new JsonObject();
        body.addProperty("currentPassword", current);
        body.addProperty("newPassword", newPass);

        progressBar.setVisibility(View.VISIBLE);
        ApiClient.getInstance(this).changePassword(body).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Password changed successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ProfileActivity.this, ApiClientError.getMessage(response), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProfileActivity.this, ApiClientError.getMessage(t), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getString(JsonObject obj, String key) {
        if (obj.has(key) && !obj.get(key).isJsonNull()) {
            return obj.get(key).getAsString();
        }
        return "";
    }
}

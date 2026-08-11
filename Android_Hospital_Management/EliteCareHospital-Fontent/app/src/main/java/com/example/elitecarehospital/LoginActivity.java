package com.example.elitecarehospital;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.elitecarehospital.network.ApiClient;
import com.example.elitecarehospital.network.ApiClientError;
import com.example.elitecarehospital.network.ApiService;
import com.example.elitecarehospital.network.LoginRequest;
import com.example.elitecarehospital.network.LoginResponse;
import com.example.elitecarehospital.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private TextView tvStatus;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(this);

        if (session.isLoggedIn()) {
            goToMain();
            return;
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
        tvStatus = findViewById(R.id.tvStatus);

        btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email required");
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password required");
            etPassword.requestFocus();
            return;
        }

        setLoading(true);
        showStatus("Connecting to server...", R.color.text_secondary);

        ApiService api = ApiClient.getInstance(this);
        api.login(new LoginRequest(email, password)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse body = response.body();
                    session.saveLogin(body.getToken(), body.getUserId(), body.getName(),
                            body.getEmail(), body.getPhone(), body.getRole(), body.getImage());
                    showStatus("Welcome " + safe(body.getName()), R.color.success);
                    btnLogin.postDelayed(() -> goToMain(), 500);
                } else {
                    String msg = ApiClientError.getMessage(response);
                    showStatus("Login failed", R.color.danger);
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                setLoading(false);
                showStatus("Cannot reach server", R.color.danger);
                Toast.makeText(LoginActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void goToMain() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!loading);
        etEmail.setEnabled(!loading);
        etPassword.setEnabled(!loading);
    }

    private void showStatus(String msg, int colorRes) {
        tvStatus.setText(msg);
        tvStatus.setTextColor(getResources().getColor(colorRes, getTheme()));
        tvStatus.setVisibility(View.VISIBLE);
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}

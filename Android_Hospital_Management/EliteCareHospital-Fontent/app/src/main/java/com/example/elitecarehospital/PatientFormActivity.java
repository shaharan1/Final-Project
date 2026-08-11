package com.example.elitecarehospital;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.elitecarehospital.network.ApiClient;
import com.example.elitecarehospital.network.ApiClientError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientFormActivity extends AppCompatActivity {

    private EditText etName, etGender, etDob, etBloodGroup, etMaritalStatus,
            etPhone, etEmail, etAddress, etCity, etDistrict;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        etName = findViewById(R.id.etName);
        etGender = findViewById(R.id.etGender);
        etDob = findViewById(R.id.etDob);
        etBloodGroup = findViewById(R.id.etBloodGroup);
        etMaritalStatus = findViewById(R.id.etMaritalStatus);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etCity = findViewById(R.id.etCity);
        etDistrict = findViewById(R.id.etDistrict);
        progressBar = findViewById(R.id.progressBar);
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> savePatient());
    }

    private void savePatient() {
        String name = etName.getText().toString().trim();
        if (name.isEmpty()) {
            etName.setError("Name required");
            return;
        }

        JsonObject body = new JsonObject();
        body.addProperty("name", name);
        put(body, "gender", etGender);
        put(body, "dateOfBirth", etDob);
        put(body, "bloodGroup", etBloodGroup);
        put(body, "maritalStatus", etMaritalStatus);
        put(body, "phone", etPhone);
        put(body, "email", etEmail);
        put(body, "address", etAddress);
        put(body, "city", etCity);
        put(body, "district", etDistrict);

        progressBar.setVisibility(View.VISIBLE);
        ApiClient.getInstance(this).post("api/patients", body).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    String patientCode = "";
                    if (response.body().isJsonObject() && response.body().getAsJsonObject().has("patientCode")) {
                        patientCode = response.body().getAsJsonObject().get("patientCode").getAsString();
                    }
                    new android.app.AlertDialog.Builder(PatientFormActivity.this)
                            .setTitle("Patient Created")
                            .setMessage("Patient Code: " + patientCode)
                            .setPositiveButton("OK", (d, w) -> finish())
                            .show();
                } else {
                    Toast.makeText(PatientFormActivity.this,
                            ApiClientError.getMessage(response), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(PatientFormActivity.this,
                        ApiClientError.getMessage(t), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void put(JsonObject obj, String key, EditText et) {
        String s = et.getText().toString().trim();
        if (!s.isEmpty()) obj.addProperty(key, s);
    }
}

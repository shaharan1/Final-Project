package com.example.elitecarehospital;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.elitecarehospital.network.ApiClient;
import com.example.elitecarehospital.network.ApiClientError;
import com.example.elitecarehospital.network.ApiService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookAppointmentActivity extends AppCompatActivity {

    private Spinner spinnerDoctor, spinnerPayment;
    private EditText etPatientName, etMobile, etProblem, etDate, etTime, etTransaction;
    private TextView tvRate;
    private Button btnCheckRate, btnConfirm;
    private ProgressBar progressBar;

    private final List<JsonObject> doctors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        spinnerDoctor = findViewById(R.id.spinnerDoctor);
        spinnerPayment = findViewById(R.id.spinnerPayment);
        etPatientName = findViewById(R.id.etPatientName);
        etMobile = findViewById(R.id.etMobile);
        etProblem = findViewById(R.id.etProblem);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etTransaction = findViewById(R.id.etTransaction);
        tvRate = findViewById(R.id.tvRate);
        btnCheckRate = findViewById(R.id.btnCheckRate);
        btnConfirm = findViewById(R.id.btnConfirm);
        progressBar = findViewById(R.id.progressBar);

        ArrayAdapter<String> paymentAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item,
                new String[]{"Cash", "bKash", "Bank"});
        paymentAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerPayment.setAdapter(paymentAdapter);

        btnCheckRate.setOnClickListener(v -> checkRate());
        btnConfirm.setOnClickListener(v -> confirmBooking());

        loadDoctors();
    }

    private void loadDoctors() {
        progressBar.setVisibility(View.VISIBLE);
        ApiClient.getInstance(this).get("api/doctors").enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressBar.setVisibility(View.GONE);
                doctors.clear();
                if (response.isSuccessful() && response.body() != null && response.body().isJsonArray()) {
                    JsonArray arr = response.body().getAsJsonArray();
                    List<String> labels = new ArrayList<>();
                    for (JsonElement el : arr) {
                        JsonObject obj = el.getAsJsonObject();
                        doctors.add(obj);
                        labels.add(str(obj, "name") + " - " + str(obj, "specialization"));
                    }
                    ArrayAdapter<String> doctorAdapter = new ArrayAdapter<>(BookAppointmentActivity.this,
                            R.layout.spinner_item, labels);
                    doctorAdapter.setDropDownViewResource(R.layout.spinner_item);
                    spinnerDoctor.setAdapter(doctorAdapter);
                } else {
                    Toast.makeText(BookAppointmentActivity.this,
                            ApiClientError.getMessage(response), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(BookAppointmentActivity.this,
                        ApiClientError.getMessage(t), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkRate() {
        String phone = etMobile.getText().toString().trim();
        int doctorPos = spinnerDoctor.getSelectedItemPosition();
        if (phone.isEmpty()) {
            etMobile.setError("Mobile number required");
            return;
        }
        if (doctorPos < 0 || doctors.isEmpty()) {
            Toast.makeText(this, "Select a doctor", Toast.LENGTH_SHORT).show();
            return;
        }
        long doctorId = doctors.get(doctorPos).get("id").getAsLong();

        progressBar.setVisibility(View.VISIBLE);
        ApiClient.getInstance(this)
                .getWithParams("api/public/checkout/calculate-rate",
                        java.util.Map.of("phone", phone, "doctorId", String.valueOf(doctorId)))
                .enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            JsonObject obj = response.body().getAsJsonObject();
                            String fee = str(obj, "fee");
                            String status = str(obj, "status");
                            tvRate.setText("Consultation Fee: " + fee + "  |  " + status);
                            tvRate.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(BookAppointmentActivity.this,
                                    ApiClientError.getMessage(response), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(BookAppointmentActivity.this,
                                ApiClientError.getMessage(t), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void confirmBooking() {
        if (doctors.isEmpty()) {
            Toast.makeText(this, "Doctors not loaded yet", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = etPatientName.getText().toString().trim();
        String phone = etMobile.getText().toString().trim();
        String problem = etProblem.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String paymentMethod = spinnerPayment.getSelectedItem().toString();
        String transactionId = etTransaction.getText().toString().trim();

        if (name.isEmpty()) { etPatientName.setError("Required"); return; }
        if (phone.isEmpty()) { etMobile.setError("Required"); return; }
        if (date.isEmpty()) { etDate.setError("yyyy-MM-dd"); return; }
        if (time.isEmpty()) { etTime.setError("HH:mm"); return; }

        long doctorId = doctors.get(spinnerDoctor.getSelectedItemPosition()).get("id").getAsLong();

        JsonObject body = new JsonObject();
        body.addProperty("patientName", name);
        body.addProperty("mobileNumber", phone);
        body.addProperty("problemDescription", problem);
        body.addProperty("doctorId", doctorId);
        body.addProperty("appointmentDate", date);
        body.addProperty("appointmentTime", time);
        body.addProperty("paymentMethod", paymentMethod);
        body.addProperty("transactionId", transactionId);

        progressBar.setVisibility(View.VISIBLE);
        ApiClient.getInstance(this)
                .post("api/public/checkout/confirm-booking", body)
                .enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            String apptNumber = str(response.body().getAsJsonObject(), "appointmentNumber");
                            new android.app.AlertDialog.Builder(BookAppointmentActivity.this)
                                    .setTitle("Booking Confirmed")
                                    .setMessage("Appointment Number: " + apptNumber)
                                    .setPositiveButton("OK", (d, w) -> finish())
                                    .show();
                        } else {
                            Toast.makeText(BookAppointmentActivity.this,
                                    ApiClientError.getMessage(response), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(BookAppointmentActivity.this,
                                ApiClientError.getMessage(t), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private String str(JsonObject obj, String key) {
        if (obj.has(key) && !obj.get(key).isJsonNull()) {
            return obj.get(key).getAsString();
        }
        return "";
    }
}

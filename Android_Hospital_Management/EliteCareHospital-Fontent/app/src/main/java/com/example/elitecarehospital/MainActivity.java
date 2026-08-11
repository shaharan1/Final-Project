package com.example.elitecarehospital;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elitecarehospital.common.DetailActivity;
import com.example.elitecarehospital.common.ListActivity;
import com.example.elitecarehospital.network.ApiClient;
import com.example.elitecarehospital.network.ApiClientError;
import com.example.elitecarehospital.network.ApiService;
import com.example.elitecarehospital.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int TYPE_LIST = 0;
    private static final int TYPE_DETAIL = 1;
    private static final int TYPE_ACTIVITY = 2;

    private DrawerLayout drawer;
    private SessionManager session;
    private TextView tvGreeting, tvRole, tvUserName, tvUserEmail;
    private RecyclerView menuList;
    private MenuAdapter adapter;
    private ProgressBar progressBar;
    private final List<MenuLink> menus = new ArrayList<>();

    static class MenuLink {
        String title;
        String icon;
        int type;
        String endpoint;
        String titleFields;
        String subtitleFields;
        Class<?> activity;
        String queryJson;

        MenuLink(String title, String icon, int type, String endpoint,
                 String titleFields, String subtitleFields, Class<?> activity, String queryJson) {
            this.title = title;
            this.icon = icon;
            this.type = type;
            this.endpoint = endpoint;
            this.titleFields = titleFields;
            this.subtitleFields = subtitleFields;
            this.activity = activity;
            this.queryJson = queryJson;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(this);
        if (!session.isLoggedIn()) {
            goToLogin();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Elite Care Hospital");
        }

        drawer = findViewById(R.id.drawerLayout);
        NavigationView navView = findViewById(R.id.navView);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        View header = navView.getHeaderView(0);
        tvUserName = header.findViewById(R.id.tvUserName);
        tvUserEmail = header.findViewById(R.id.tvUserEmail);
        tvGreeting = findViewById(R.id.tvGreeting);
        tvRole = findViewById(R.id.tvRole);
        menuList = findViewById(R.id.menuList);
        progressBar = findViewById(R.id.progressBar);

        tvUserName.setText(session.getName());
        tvUserEmail.setText(session.getEmail());

        adapter = new MenuAdapter(menus, link -> openLink(link));
        menuList.setLayoutManager(new LinearLayoutManager(this));
        menuList.setAdapter(adapter);

        navView.setNavigationItemSelectedListener(item -> {
            drawer.closeDrawer(GravityCompat.START);
            if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
            } else if (item.getItemId() == R.id.nav_logout) {
                logout();
            }
            return true;
        });

        buildMenus();
        render();
    }

    private void buildMenus() {
        menus.clear();
        String role = session.getRole();
        long userId = session.getUserId();

        if (role == null || role.isEmpty()) {
            role = "Admin";
        }

        switch (role) {
            case "Admin":
                menus.add(new MenuLink("Billing Dashboard", "💰", TYPE_DETAIL,
                        "api/billing-dashboard/summary", "[\"totalRevenue\"]",
                        "[\"date\"]", null, null));
                menus.add(new MenuLink("Patients", "👤", TYPE_LIST, "api/patients",
                        "[\"name\",\"patientCode\"]", "[\"phone\",\"bloodGroup\",\"gender\"]", null, null));
                menus.add(new MenuLink("Doctors", "🩺", TYPE_LIST, "api/doctors",
                        "[\"name\",\"designation\"]", "[\"specialization\",\"phone\"]", null, null));
                menus.add(new MenuLink("Nurses", "💉", TYPE_LIST, "api/nurses",
                        "[\"name\",\"nurseType\"]", "[\"assignedWard\",\"phone\"]", null, null));
                menus.add(new MenuLink("Appointments", "📅", TYPE_LIST, "api/appointments",
                        "[\"appointmentNumber\",\"patientName\"]", "[\"doctorName\",\"appointmentDate\",\"status\"]", null, null));
                menus.add(new MenuLink("Doctor Departments", "🏥", TYPE_LIST, "api/doctor-departments",
                        "[\"departmentName\"]", "[\"description\"]", null, null));
                menus.add(new MenuLink("Office Staff", "🧑‍💼", TYPE_LIST, "api/office-staff",
                        "[\"name\",\"position\"]", "[\"department\",\"phone\"]", null, null));
                menus.add(new MenuLink("Billing Invoices", "🧾", TYPE_LIST, "api/billing-invoices",
                        "[\"invoiceNumber\",\"patientName\"]", "[\"netAmount\",\"paymentStatus\"]", null, null));
                menus.add(new MenuLink("Wards", "🛏️", TYPE_LIST, "api/infrastructure/wards",
                        "[\"name\",\"roomType\"]", "[\"departmentName\",\"totalBeds\"]", null, null));
                menus.add(new MenuLink("Beds", "🛏️", TYPE_LIST, "api/infrastructure/beds",
                        "[\"bedNumber\",\"status\"]", "[\"wardName\"]", null, null));
                menus.add(new MenuLink("Book Appointment", "➕", TYPE_ACTIVITY, null,
                        null, null, BookAppointmentActivity.class, null));
                menus.add(new MenuLink("My Profile", "👤", TYPE_ACTIVITY, null,
                        null, null, ProfileActivity.class, null));
                break;

            case "Doctor":
                resolveDoctorAndAdd(userId);
                menus.add(new MenuLink("All Appointments", "📅", TYPE_LIST, "api/appointments",
                        "[\"appointmentNumber\",\"patientName\"]", "[\"doctorName\",\"appointmentDate\",\"status\"]", null, null));
                menus.add(new MenuLink("Prescriptions", "💊", TYPE_LIST, "api/prescriptions",
                        "[\"prescriptionNumber\",\"patientName\"]", "[\"doctorName\",\"diagnosis\"]", null, null));
                menus.add(new MenuLink("Patients", "👤", TYPE_LIST, "api/patients",
                        "[\"name\",\"patientCode\"]", "[\"phone\",\"bloodGroup\"]", null, null));
                menus.add(new MenuLink("Test Orders", "🔬", TYPE_LIST, "api/test-orders",
                        "[\"orderNumber\",\"patientName\"]", "[\"status\",\"testName\"]", null, null));
                menus.add(new MenuLink("Book Appointment", "➕", TYPE_ACTIVITY, null,
                        null, null, BookAppointmentActivity.class, null));
                menus.add(new MenuLink("My Profile", "👤", TYPE_ACTIVITY, null,
                        null, null, ProfileActivity.class, null));
                break;

            case "Receptionist":
                menus.add(new MenuLink("Appointments", "📅", TYPE_LIST, "api/appointments",
                        "[\"appointmentNumber\",\"patientName\"]", "[\"doctorName\",\"appointmentDate\",\"status\"]", null, null));
                menus.add(new MenuLink("Book Appointment", "➕", TYPE_ACTIVITY, null,
                        null, null, BookAppointmentActivity.class, null));
                menus.add(new MenuLink("Patients", "👤", TYPE_LIST, "api/patients",
                        "[\"name\",\"patientCode\"]", "[\"phone\",\"bloodGroup\"]", null, null));
                menus.add(new MenuLink("Add Patient", "➕", TYPE_ACTIVITY, null,
                        null, null, PatientFormActivity.class, null));
                menus.add(new MenuLink("Doctors", "🩺", TYPE_LIST, "api/doctors",
                        "[\"name\",\"designation\"]", "[\"specialization\",\"phone\"]", null, null));
                menus.add(new MenuLink("My Profile", "👤", TYPE_ACTIVITY, null,
                        null, null, ProfileActivity.class, null));
                break;

            case "Nurse":
                menus.add(new MenuLink("Active Admissions", "🏥", TYPE_LIST, "api/admissions/active",
                        "[\"patientName\",\"admissionId\"]", "[\"wardName\",\"assignedBedNumber\",\"status\"]", null, null));
                menus.add(new MenuLink("Emergency Patients", "🚑", TYPE_LIST, "api/emergency/patients",
                        "[\"patientName\",\"triageLevel\"]", "[\"severityLevel\",\"arrivalTime\"]", null, null));
                menus.add(new MenuLink("Triage Records", "🩺", TYPE_LIST, "api/emergency/triage",
                        "[\"emergencyNumber\",\"triageLevel\"]", "[\"assessedBy\"]", null, null));
                menus.add(new MenuLink("Patients", "👤", TYPE_LIST, "api/patients",
                        "[\"name\",\"patientCode\"]", "[\"phone\",\"bloodGroup\"]", null, null));
                menus.add(new MenuLink("Wards", "🛏️", TYPE_LIST, "api/infrastructure/wards",
                        "[\"wardName\",\"wardNumber\"]", "[\"departmentName\"]", null, null));
                menus.add(new MenuLink("My Profile", "👤", TYPE_ACTIVITY, null,
                        null, null, ProfileActivity.class, null));
                break;

            case "Pharmacist":
                menus.add(new MenuLink("Pharmacy Dashboard", "📊", TYPE_DETAIL, "api/pharmacy/dashboard",
                        null, null, null, null));
                menus.add(new MenuLink("Medicine Stock", "💊", TYPE_LIST, "api/pharmacy/stock",
                        "[\"medicineName\"]", "[\"batchNumber\",\"availableQuantity\",\"expiryDate\"]", null, null));
                menus.add(new MenuLink("Medicines", "🧪", TYPE_LIST, "api/medicines",
                        "[\"medicineName\",\"genericName\"]", "[\"dosage\"]", null, null));
                menus.add(new MenuLink("Pharmacy Sales", "🛒", TYPE_LIST, "api/pharmacy/sales",
                        "[\"saleInvoiceNo\",\"patientName\"]", "[\"netPayable\",\"paymentStatus\"]", null, null));
                menus.add(new MenuLink("My Profile", "👤", TYPE_ACTIVITY, null,
                        null, null, ProfileActivity.class, null));
                break;

            case "LabTechnician":
                menus.add(new MenuLink("Test Orders", "🔬", TYPE_LIST, "api/test-orders",
                        "[\"testCode\",\"patientName\"]", "[\"testName\",\"orderStatus\"]", null, null));
                menus.add(new MenuLink("Test Masters", "🧫", TYPE_LIST, "api/tests",
                        "[\"testName\",\"testCode\"]", "[\"standardPrice\"]", null, null));
                menus.add(new MenuLink("My Profile", "👤", TYPE_ACTIVITY, null,
                        null, null, ProfileActivity.class, null));
                break;

            case "BillingClerk":
                menus.add(new MenuLink("Billing Dashboard", "📊", TYPE_DETAIL, "api/billing-dashboard/summary",
                        null, null, null, null));
                menus.add(new MenuLink("Invoices", "🧾", TYPE_LIST, "api/billing-invoices",
                        "[\"invoiceNumber\",\"patientName\"]", "[\"netAmount\",\"paymentStatus\"]", null, null));
                menus.add(new MenuLink("Payments", "💳", TYPE_LIST, "api/payments",
                        "[\"invoiceNumber\",\"patientName\"]", "[\"amount\",\"paymentMethod\",\"paymentStatus\"]", null, null));
                menus.add(new MenuLink("Other Charges", "💸", TYPE_LIST, "api/charges/others",
                        "[\"description\"]", "[\"amount\",\"category\"]", null, null));
                menus.add(new MenuLink("My Profile", "👤", TYPE_ACTIVITY, null,
                        null, null, ProfileActivity.class, null));
                break;

            case "InventoryManager":
                menus.add(new MenuLink("Medicine Stock", "💊", TYPE_LIST, "api/pharmacy/stock",
                        "[\"medicineName\"]", "[\"batchNumber\",\"availableQuantity\",\"expiryDate\"]", null, null));
                menus.add(new MenuLink("Medicines", "🧪", TYPE_LIST, "api/medicines",
                        "[\"medicineName\",\"genericName\"]", "[\"dosage\"]", null, null));
                menus.add(new MenuLink("Suppliers", "🚚", TYPE_LIST, "api/pharmacy/suppliers",
                        "[\"name\",\"contactPerson\"]", "[\"phone\",\"email\"]", null, null));
                menus.add(new MenuLink("Purchases", "📦", TYPE_LIST, "api/purchases",
                        "[\"invoiceNo\",\"supplierName\"]", "[\"netAmount\",\"status\"]", null, null));
                menus.add(new MenuLink("My Profile", "👤", TYPE_ACTIVITY, null,
                        null, null, ProfileActivity.class, null));
                break;

            case "WardManager":
                menus.add(new MenuLink("Active Admissions", "🏥", TYPE_LIST, "api/admissions/active",
                        "[\"patientName\",\"admissionId\"]", "[\"wardName\",\"assignedBedNumber\",\"status\"]", null, null));
                menus.add(new MenuLink("Wards", "🛏️", TYPE_LIST, "api/infrastructure/wards",
                        "[\"name\",\"roomType\"]", "[\"departmentName\",\"totalBeds\"]", null, null));
                menus.add(new MenuLink("Beds", "🛏️", TYPE_LIST, "api/infrastructure/beds",
                        "[\"bedNumber\",\"status\"]", "[\"wardName\"]", null, null));
                menus.add(new MenuLink("Meal Schedules", "🍽️", TYPE_LIST, "api/meal-schedules",
                        "[\"mealName\"]", "[\"servingTime\",\"status\"]", null, null));
                menus.add(new MenuLink("My Profile", "👤", TYPE_ACTIVITY, null,
                        null, null, ProfileActivity.class, null));
                break;

            case "Dietician":
                menus.add(new MenuLink("Diet Plans", "🥗", TYPE_LIST, "api/diet-plans",
                        "[\"name\",\"dietType\"]", "[\"description\"]", null, null));
                menus.add(new MenuLink("Diet Assignments", "🍱", TYPE_LIST, "api/diet-assignments",
                        "[\"wardName\",\"bedNumber\"]", "[\"status\"]", null, null));
                menus.add(new MenuLink("Diet History", "📖", TYPE_LIST, "api/diet-history",
                        "[\"actionType\"]", "[\"description\"]", null, null));
                menus.add(new MenuLink("Meals", "🍽️", TYPE_LIST, "api/v1/meals",
                        "[\"mealMasterName\"]", "[\"billingStatus\"]", null, null));
                menus.add(new MenuLink("My Profile", "👤", TYPE_ACTIVITY, null,
                        null, null, ProfileActivity.class, null));
                break;

            case "OfficeStaff":
                menus.add(new MenuLink("Patients", "👤", TYPE_LIST, "api/patients",
                        "[\"name\",\"patientCode\"]", "[\"phone\",\"bloodGroup\"]", null, null));
                menus.add(new MenuLink("Appointments", "📅", TYPE_LIST, "api/appointments",
                        "[\"appointmentNumber\",\"patientName\"]", "[\"doctorName\",\"appointmentDate\",\"status\"]", null, null));
                menus.add(new MenuLink("Doctors", "🩺", TYPE_LIST, "api/doctors",
                        "[\"name\",\"designation\"]", "[\"specialization\",\"phone\"]", null, null));
                menus.add(new MenuLink("Admissions", "🏥", TYPE_LIST, "api/admissions",
                        "[\"patientName\",\"admissionId\"]", "[\"wardName\",\"assignedBedNumber\",\"status\"]", null, null));
                menus.add(new MenuLink("My Profile", "👤", TYPE_ACTIVITY, null,
                        null, null, ProfileActivity.class, null));
                break;

            default:
                menus.add(new MenuLink("Patients", "👤", TYPE_LIST, "api/patients",
                        "[\"name\",\"patientCode\"]", "[\"phone\",\"bloodGroup\"]", null, null));
                menus.add(new MenuLink("Doctors", "🩺", TYPE_LIST, "api/doctors",
                        "[\"name\",\"designation\"]", "[\"specialization\",\"phone\"]", null, null));
                menus.add(new MenuLink("Appointments", "📅", TYPE_LIST, "api/appointments",
                        "[\"appointmentNumber\",\"patientName\"]", "[\"doctorName\",\"appointmentDate\",\"status\"]", null, null));
                menus.add(new MenuLink("My Profile", "👤", TYPE_ACTIVITY, null,
                        null, null, ProfileActivity.class, null));
                break;
        }
    }

    private void resolveDoctorAndAdd(long userId) {
        progressBar.setVisibility(View.VISIBLE);
        ApiClient.getInstance(this).get("api/doctors/user/" + userId)
                .enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null
                                && response.body().isJsonObject()) {
                            long doctorId = response.body().getAsJsonObject().get("id").getAsLong();
                            menus.add(0, new MenuLink("My Appointments", "🩺", TYPE_LIST,
                                    "api/appointments/doctor/" + doctorId,
                                    "[\"appointmentNumber\",\"patientName\"]",
                                    "[\"appointmentDate\",\"status\"]", null, null));
                        } else {
                            menus.add(0, new MenuLink("My Appointments", "🩺", TYPE_LIST,
                                    "api/appointments",
                                    "[\"appointmentNumber\",\"patientName\"]",
                                    "[\"appointmentDate\",\"status\"]", null, null));
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        menus.add(0, new MenuLink("My Appointments", "🩺", TYPE_LIST,
                                "api/appointments",
                                "[\"appointmentNumber\",\"patientName\"]",
                                "[\"appointmentDate\",\"status\"]", null, null));
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void render() {
        String role = session.getRole();
        String name = session.getName();
        tvGreeting.setText("Welcome, " + (name == null || name.isEmpty() ? "User" : name) + " 👋");
        tvRole.setText("Role: " + (role == null || role.isEmpty() ? "-" : role));
        adapter.notifyDataSetChanged();
    }

    private void openLink(MenuLink link) {
        switch (link.type) {
            case TYPE_LIST: {
                Intent i = new Intent(this, ListActivity.class);
                i.putExtra(ListActivity.EXTRA_TITLE, link.title);
                i.putExtra(ListActivity.EXTRA_ENDPOINT, link.endpoint);
                i.putExtra(ListActivity.EXTRA_TITLE_FIELDS, link.titleFields);
                i.putExtra(ListActivity.EXTRA_SUBTITLE_FIELDS, link.subtitleFields);
                if (link.queryJson != null) {
                    i.putExtra(ListActivity.EXTRA_QUERY_JSON, link.queryJson);
                }
                startActivity(i);
                break;
            }
            case TYPE_DETAIL: {
                Intent i = new Intent(this, DetailActivity.class);
                i.putExtra(DetailActivity.EXTRA_TITLE, link.title);
                i.putExtra(DetailActivity.EXTRA_ENDPOINT, link.endpoint);
                startActivity(i);
                break;
            }
            case TYPE_ACTIVITY: {
                if (link.activity != null) {
                    startActivity(new Intent(this, link.activity));
                }
                break;
            }
        }
    }

    private void logout() {
        session.clear();
        ApiClient.reset();
        goToLogin();
    }

    private void goToLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    static class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.VH> {

        interface OnMenuClick {
            void onClick(MenuLink link);
        }

        private final List<MenuLink> data;
        private final OnMenuClick listener;

        MenuAdapter(List<MenuLink> data, OnMenuClick listener) {
            this.data = data;
            this.listener = listener;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = android.view.LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_menu, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            MenuLink link = data.get(position);
            holder.tvIcon.setText(link.icon);
            holder.tvTitle.setText(link.title);
            holder.itemView.setOnClickListener(v -> listener.onClick(link));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvIcon, tvTitle;

            VH(View itemView) {
                super(itemView);
                tvIcon = itemView.findViewById(R.id.tvMenuIcon);
                tvTitle = itemView.findViewById(R.id.tvMenuTitle);
            }
        }
    }
}

package com.example.elitecarehospital.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "elite_care_session";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ROLE = "role";
    private static final String KEY_IMAGE = "image";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveLogin(String token, Long userId, String name, String email,
                          String phone, String role, String image) {
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putLong(KEY_USER_ID, userId == null ? -1L : userId)
                .putString(KEY_NAME, name)
                .putString(KEY_EMAIL, email)
                .putString(KEY_PHONE, phone)
                .putString(KEY_ROLE, role)
                .putString(KEY_IMAGE, image)
                .apply();
    }

    public boolean isLoggedIn() {
        String token = getToken();
        return token != null && !token.isEmpty();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public long getUserId() {
        return prefs.getLong(KEY_USER_ID, -1L);
    }

    public String getName() {
        return prefs.getString(KEY_NAME, "");
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, "");
    }

    public String getPhone() {
        return prefs.getString(KEY_PHONE, "");
    }

    public String getRole() {
        return prefs.getString(KEY_ROLE, "");
    }

    public String getImage() {
        return prefs.getString(KEY_IMAGE, "");
    }

    public void updateProfile(String name, String phone) {
        prefs.edit()
                .putString(KEY_NAME, name)
                .putString(KEY_PHONE, phone)
                .apply();
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}

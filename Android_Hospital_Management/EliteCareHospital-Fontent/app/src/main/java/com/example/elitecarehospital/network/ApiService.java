package com.example.elitecarehospital.network;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiService {

    // ── Auth ──────────────────────────────────────────────
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest body);

    @GET("api/profile")
    Call<JsonElement> getProfile();

    @PUT("api/profile")
    Call<JsonElement> updateProfile(@Body JsonObject body);

    @PUT("api/profile/password")
    Call<JsonElement> changePassword(@Body JsonObject body);

    // ── Generic GET / POST / PUT / DELETE ─────────────────
    // Accepts a full URL (base + relative) e.g. "api/patients"
    @GET
    Call<JsonElement> get(@Url String url);

    @GET
    Call<JsonElement> getWithParams(@Url String url, @QueryMap Map<String, String> params);

    @POST
    Call<JsonElement> post(@Url String url, @Body JsonElement body);

    @PUT
    Call<JsonElement> put(@Url String url, @Body JsonElement body);

    @DELETE
    Call<JsonElement> delete(@Url String url);

    @PUT
    Call<JsonElement> putVoid(@Url String url);

    @POST
    Call<JsonElement> postForm(@Url String url, @Body JsonElement body);
}

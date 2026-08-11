package com.example.elitecarehospital.network;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("token")
    private String token;

    @SerializedName("tokenType")
    private String tokenType;

    @SerializedName("userId")
    private Long userId;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("role")
    private String role;

    @SerializedName("image")
    private String image;

    public String getToken() { return token; }
    public String getTokenType() { return tokenType; }
    public Long getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }
    public String getImage() { return image; }
}

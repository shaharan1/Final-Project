package com.example.elitecarehospital.network;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import retrofit2.Response;

public class ApiClientError {

    public static String getMessage(Response<?> response) {
        if (response.code() == 401) {
            return "Invalid email or password.";
        }
        String body = response.errorBody() == null ? null : readBody(response);
        if (body != null && !body.isEmpty()) {
            try {
                JsonElement el = JsonParser.parseString(body);
                if (el.isJsonObject()) {
                    JsonObject obj = el.getAsJsonObject();
                    if (obj.has("message")) return obj.get("message").getAsString();
                    if (obj.has("error")) return obj.get("error").getAsString();
                    if (obj.has("detail")) return obj.get("detail").getAsString();
                }
            } catch (JsonSyntaxException ignored) {
            }
            if (body.length() < 200) return body;
        }
        return "Request failed (" + response.code() + ")";
    }

    public static String getMessage(Throwable t) {
        if (t instanceof java.net.ConnectException) {
            return "Cannot connect to server. Make sure backend is running on port 8085.";
        }
        if (t instanceof java.net.SocketTimeoutException) {
            return "Server took too long to respond.";
        }
        if (t instanceof java.net.UnknownHostException) {
            return "Server address not found.";
        }
        return t.getMessage() == null ? "Unknown error" : t.getMessage();
    }

    private static String readBody(Response<?> response) {
        try {
            okhttp3.ResponseBody errorBody = (okhttp3.ResponseBody) response.errorBody();
            if (errorBody == null) return null;
            String s = errorBody.string();
            if (errorBody != null) errorBody.close();
            return s;
        } catch (Exception e) {
            return null;
        }
    }
}

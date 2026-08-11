package com.example.elitecarehospital.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final TokenProvider tokenProvider;

    public interface TokenProvider {
        String getToken();
    }

    public AuthInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        String token = tokenProvider.getToken();

        Request.Builder builder = original.newBuilder()
                .header("Accept", "application/json");

        if (token != null && !token.isEmpty()) {
            builder.header("Authorization", "Bearer " + token);
        }

        return chain.proceed(builder.build());
    }
}

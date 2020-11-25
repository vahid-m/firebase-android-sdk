package com.google.firebase.crashlytics.internal.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.FirebaseOptions;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Vahid Mohammadi on 11/4/2020.
 */
public class OkHttpProvider {
    private static final int DEFAULT_TIMEOUT_MS = 10 * 1000; // 10 seconds in millis
    private static final Object LOCK = new Object();
    private static OkHttpClient CLIENT;

    public static void init(@Nullable FirebaseOptions options) {
        synchronized (LOCK) {
            if (CLIENT == null) {
                CLIENT =
                        new OkHttpClient()
                                .newBuilder()
                                .proxy(options != null ? options.getProxy() : null)
                                .callTimeout(DEFAULT_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                                .build();
            }
        }
    }

    @NonNull
    public static OkHttpClient getInstance() {
        synchronized (LOCK) {
            if (CLIENT == null) {
                init(null);
            }
            return CLIENT;
        }
    }
}

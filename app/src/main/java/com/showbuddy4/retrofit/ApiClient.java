package com.showbuddy4.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jainam on 14-12-2017.
 */

public class ApiClient {

    public static final String BASE_URL = "http://demo.workdesirewebsolutions.com/showbuddy/";
    private static Retrofit retrofit = null;
    static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();


    public static OkHttpClient okHttpClient1 = new OkHttpClient().newBuilder()
            //           .addNetworkInterceptor(new StethoInterceptor())
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(250, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build();


    public static Retrofit getClient() {

        if (retrofit == null) {

            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient1)
                    .build();
        }

        return retrofit;
    }
}

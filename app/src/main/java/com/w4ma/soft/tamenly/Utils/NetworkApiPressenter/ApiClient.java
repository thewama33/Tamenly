package com.w4ma.soft.tamenly.Utils.NetworkApiPressenter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.w4ma.soft.tamenly.BuildConfig;

import java.io.File;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getCacheDir;

public class ApiClient {


    public static final String baseURL="https://www.amdoren.com/api/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofit(){

        int cacheSize = 20 * 1024 * 1024; // 20 MB
        Cache cache = new Cache(getCacheDir(), cacheSize);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        if (BuildConfig.DEBUG) {
            // development build
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            // production build
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .cache(cache)
                .build();

        if (retrofit==null){

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .baseUrl(baseURL)
                    .build();

        }


        return retrofit;
    }

}

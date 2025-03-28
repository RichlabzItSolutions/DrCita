package com.drcita.user.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
   // private static final String BASE_URL = "https://caafisom.com/api/";
    private static final String BASE_URL = "https://drcita.com/api/";


    private static ApiInterface restAPI;

    private static void init() {
        //ProviderInstaller.installIfNeeded(ITutorSource);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS);
      
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(
                        RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        restAPI = retrofit.create(ApiInterface.class);
    }
    public static ApiInterface getRestAPI() {
        if (restAPI == null) init();
        return restAPI;
    }
}

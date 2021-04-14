package com.catasoft.ip_finder.data.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ApiBuilder {

    private static ApiService apiBuilder;
    private final static String BASE_URL = "http://ip-api.com/";

    public static ApiService getInstance(){
        if(apiBuilder == null){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(MoshiConverterFactory.create().asLenient())
                    .build();
            apiBuilder = retrofit.create(ApiService.class);
        }
        return apiBuilder;
    }

}

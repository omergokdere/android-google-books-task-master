package com.ersen.faxitest.application;

import android.app.Application;

import com.ersen.faxitest.connection.GoogleAPI;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class Moblib extends Application {
    private static Moblib sInstance; //Get this application instance
    private Retrofit mRetrofitClient;
    private GoogleAPI mNetworkAPI;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static Moblib getInstance() {
        return sInstance;
    }

    public GoogleAPI getNetworkAPI(){
        if(mNetworkAPI == null){
            mNetworkAPI = getRetrofitClient().create(GoogleAPI.class);
        }
        return  mNetworkAPI;
    }

    private Retrofit getRetrofitClient(){
        if (mRetrofitClient == null){
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
            okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);

            mRetrofitClient = new Retrofit.Builder()
                    .baseUrl(MobLibConstant.URLConstants.GOOGLE_BOOKS_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }

        return mRetrofitClient;
    }
}

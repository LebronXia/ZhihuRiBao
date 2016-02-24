package com.xiamu.riane.zhihuribao.inject.module.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiamu.riane.zhihuribao.protocol.ClientApi;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

/**
 * Created by Xiamu on 2016/2/1.
 */
@Module
public class ClientApiModule {

    private static final String API_VERSION = "4";
    private static final String BASE_URL = "http://news-at.zhihu.com/api/4/";

    @Provides
    @Singleton
    public ClientApi provideClientApi(OkHttpClient client, Converter.Factory converterFactory){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        return  retrofit.create(ClientApi.class);
    }

    @Provides
    @Singleton
    public HttpLoggingInterceptor providerLog(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        return  interceptor;
    }

    @Provides
    @Singleton
    public Converter.Factory providerConverter(Gson gson){
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    public Gson providerGson(){
        return  new GsonBuilder().serializeNulls().create();
    }

    @Provides
    @Singleton
    public OkHttpClient providerClient(HttpLoggingInterceptor loggingInterceptor){
        OkHttpClient client = new OkHttpClient();
        List<Interceptor> list = new ArrayList<Interceptor>(client.interceptors());
        list.add(loggingInterceptor);
        return client;
    }
}

package com.vpigadas.vivawallet.service;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.vpigadas.vivawallet.AppDatabase;
import com.vpigadas.vivawallet.models.Items;
import com.vpigadas.vivawallet.models.Order;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

public class ServerClient {

    private static final String PRODUCTION_URL = "https://vivawallet.free.beeceptor.com//";

    private static ServerClient instance;

    private AppDatabase appDatabase;
    private OkHttpClient.Builder httpClient;
    private Retrofit.Builder retroBuilder;


    public static ServerClient getIntance(Context context) {
        if (instance == null) {
            instance = new ServerClient();
            instance.appDatabase = Room.databaseBuilder(context,
                    AppDatabase.class, "viva-wallet").build();
        }

        return instance;

    }

    private Retrofit.Builder getRetrofitInstance() {
        if (retroBuilder == null) {
            retroBuilder = new Retrofit.Builder();
            retroBuilder.baseUrl(PRODUCTION_URL);

            Gson gson = new GsonBuilder().setLenient().create();

            retroBuilder
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson));
        }

        return retroBuilder;
    }

    private OkHttpClient getHttpInstance() {
        if (httpClient == null) {

            String authToken = Credentials.basic("9e81ed8b-0844-4805-b93d-d6d83569814f", "123456");
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);


            httpClient = new OkHttpClient.Builder();
            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);
            }
        }

        return httpClient.build();
    }

    private <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = getRetrofitInstance().client(getHttpInstance()).build();

        return retrofit.create(serviceClass);
    }

    @SuppressLint("CheckResult")
    public void getCashedProducts(final MutableLiveData<List<Items>> handlerManager, final MutableLiveData<Throwable> handlerError) {
        appDatabase.products().getAll().observeForever(new Observer<List<Items>>() {
            @Override
            public void onChanged(@Nullable List<Items> items) {
                if (items == null || items.isEmpty()) {
                    getProducts(handlerManager, handlerError);
                } else {
                    handlerManager.postValue(items);
                }
            }
        });
    }

    @SuppressLint("CheckResult")
    public void getProducts(final MutableLiveData<List<Items>> handlerManager, final MutableLiveData<Throwable> handlerError) {

        ServerInterface client = createService(ServerInterface.class);
        Observable<List<Items>> call = client.getProducts();

        call.subscribeOn(Schedulers.single())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<List<Items>>() {
                    @Override
                    public void accept(List<Items> items) {
                        appDatabase.products().insertAll(items);

                        handlerManager.postValue(items);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        handlerError.postValue(throwable);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void postOrder(String amount, final MutableLiveData<List<Items>> handlerManager, final MutableLiveData<Throwable> handlerError) {

        String strAmount = amount.substring(1, amount.length()).replaceAll(",", "");

        int price = Integer.parseInt(strAmount);

        int position = amount.lastIndexOf(",");
        if (position - amount.length() == 0) {
            price = price * 100;
        } else if (position - amount.length() == 1) {
            price = price * 10;
        }


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Amount", price);
        jsonObject.addProperty("SourceCode", "Default");

        ServerInterface client = createService(ServerInterface.class);
        Observable<String> call = client.postOrder(jsonObject);

        call.subscribeOn(Schedulers.single())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String order) {
                        if (order == null) {

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        handlerError.postValue(throwable);
                    }
                });

    }

    class AuthenticationInterceptor implements Interceptor {

        private String authToken;

        public AuthenticationInterceptor(String token) {
            this.authToken = token;
        }

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            Request.Builder builder = original.newBuilder()
                    .header("Authorization", authToken);

            Request request = builder.build();
            return chain.proceed(request);
        }
    }
}

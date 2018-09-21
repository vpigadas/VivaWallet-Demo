package com.vpigadas.vivawallet.service;

import com.google.gson.JsonObject;
import com.vpigadas.vivawallet.models.Items;
import com.vpigadas.vivawallet.models.Order;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ServerInterface {

    @GET("https://vivawallet.free.beeceptor.com//v1/api/products")
    Observable<List<Items>> getProducts();

    @POST("https://vivawallet.free.beeceptor.com//v1/api/orders")
    Observable<String> postOrder(@Body JsonObject body);

    @POST("https://vivawallet.free.beeceptor.com//v1/api/cards?key=lF62E4J2tI2MLAncwZAszLzQ3Iu4RtQ9p5eRZIIttWo=")
    Observable<Object> postCard(@Body JsonObject body);

    @POST("https://vivawallet.free.beeceptor.com//v1//api/cards/installments?key=lF62E4J2tI2MLAncwZAszLzQ3Iu4RtQ9p5eRZIIttWo=")
    Observable<Object> postTransaction(@Header("CardNumber") String cardNumber);

    @POST("https://vivawallet.free.beeceptor.com//v1/api/Transactions")
    Observable<Object> postInstalments(@Body JsonObject body);

}

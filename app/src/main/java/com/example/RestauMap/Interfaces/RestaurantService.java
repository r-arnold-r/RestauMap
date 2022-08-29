package com.example.RestauMap.Interfaces;

import com.example.RestauMap.Model.listing.RestaurantPOST;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RestaurantService {

    @POST("listing.php")
    @FormUrlEncoded
    Call<RestaurantPOST> userRestaurants(@Field("udid") String udid, @Field("app_id") String app_id, @Field("content") String content, @Field("keyword") String keyword);

}

package com.example.RestauMap.Interfaces;

import com.example.RestauMap.Model.detail.RestaurantDetailPOST;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RestaurantServiceDetails {

    @POST("detail.php")
    @FormUrlEncoded
    Call<RestaurantDetailPOST> userRestaurantDetails(@Field("udid") String udid, @Field("app_id") String app_id, @Field("content") String content, @Field("cid") int cid);

}

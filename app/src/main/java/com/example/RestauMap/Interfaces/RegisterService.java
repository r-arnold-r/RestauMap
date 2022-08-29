package com.example.RestauMap.Interfaces;

import com.example.RestauMap.Model.register.RegisterPOST;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterService {

    @POST("register.php")
    @FormUrlEncoded
    Call<RegisterPOST> userRegister(@Field("udid") String udid, @Field("app_id") String app_id, @Field("email") String email, @Field("pass") String pass, @Field("name") String name);

}

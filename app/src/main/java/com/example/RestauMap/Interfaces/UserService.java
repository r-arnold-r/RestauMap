package com.example.RestauMap.Interfaces;

import com.example.RestauMap.Model.login.LoginPOST;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {

    @POST("login.php")
    @FormUrlEncoded
    Call<LoginPOST> userLogin(@Field("udid") String udid, @Field("app_id") String app_id, @Field("email") String email, @Field("pass") String password, @Field("token") String token);

}
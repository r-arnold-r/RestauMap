package com.example.RestauMap.Retrofit;

import com.example.RestauMap.Interfaces.RegisterService;
import com.example.RestauMap.Interfaces.RestaurantService;
import com.example.RestauMap.Interfaces.RestaurantServiceDetails;
import com.example.RestauMap.Interfaces.UserService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    /**
     * get Retrofit client
     */
    private static Retrofit getRetrofit(){

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        //build retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://mobcom.testplat4m.net/")
                .client(okHttpClient)
                .build();

        return  retrofit;
    }

    /**
     * userService
     */
    public static UserService getUserService(){
        UserService userService = getRetrofit().create(UserService.class);
        return userService;
    }

    /**
     * restaurantService
     */
    public static RestaurantService getRestaurantService(){
        RestaurantService restaurantService = getRetrofit().create(RestaurantService.class);
        return restaurantService;
    }

    /**
     * restaurantServiceDetails
     */
    public static RestaurantServiceDetails getRestaurantServiceDetails(){
        RestaurantServiceDetails restaurantServiceDetails = getRetrofit().create(RestaurantServiceDetails.class);
        return restaurantServiceDetails;
    }

    /**
     * registerService
     */
    public static RegisterService getRegisterService(){
        RegisterService registerService = getRetrofit().create(RegisterService.class);
        return registerService;
    }
}

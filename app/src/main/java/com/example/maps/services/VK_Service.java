package com.example.maps.services;

import com.google.gson.JsonElement;

import org.json.JSONObject;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VK_Service {
    @GET("/method/account.getProfileInfo")
    Call<JsonElement> getUserInfo(@Query("access_token")String access_token, @Query("v") String v);

}

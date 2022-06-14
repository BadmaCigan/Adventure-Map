package com.example.maps.services;

import com.example.maps.entity.EventMarker;
import com.example.maps.entity.Message;
import com.example.maps.entity.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {

    @GET("/db/deleteDb")
    public Call<Boolean> deleteDB(@Query("id")int pass);

    @GET("/db/createDb")
    public Call<Boolean> createDB();

    @GET("/marker/send")
    public Call<Void> sendMsg(@Query("eventMarker") EventMarker eventMarker);

    @GET("/user/registrateUser")
    public Call<Void> registrateUser(@Query("user")String user) ;

    @GET("/user/isRegisrated")
    public Call<Boolean> isUserRegistrated(@Query("id") int id);

    @GET("/user/isUserSubsribedOnEvent")
    public Call<Boolean> isUserSubsribedOnEvent(@Query("eventId") int eventId,@Query("userId")int userId);

    @GET("/user/getUserById")
    public Call<User> getUserById(@Query("id") int id);

    @GET("/user/deleteUser")
    public Call<Boolean> deleteUser(@Query("id") int id);

    @GET("/marker/deleteMarker")
    public Call<Boolean> deleteMarker(@Query("id") int id);


    @GET("/marker/registrationOnEvent")
    public Call<Void> registrationOnEvent(@Query("eventId") int eventId,@Query("userId")int userId);

    @GET("/test")
    public Call<Void> test();

    @GET("/marker/getMarkers")
    public Call<ArrayList<EventMarker>> getMarkers();

    @GET("/event/getEventMessages")
    public Call<ArrayList<Message>> getEventMessages(@Query("eventId") int eventId);

    @GET("/event/sendMessage")
    public Call<Void> sendMessage(@Query("message") String message);

    @GET("/event/getTags")
    public Call<ArrayList<String>> getTags();

}

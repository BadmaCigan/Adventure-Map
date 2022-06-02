package com.example.maps;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {

    //@GET("/java/test")
    //public Call<LinkedList<User>> test(@Query("firstName") String firstName, @Query("lastName") String lastName);
    @GET("/java/test")
    public Call<Void> test(@Query("firstName") String firstName, @Query("lastName") String lastName);

    @GET("/user/isRegisrated")
    public Call<Boolean> isUserRegistrated(@Query("id") int id);

    @GET("/user/registrateUser")
    public Call<Void> registrateUser(@Query("user")User user) ;


    @GET("/user/getUserById")
    public Call<User> getUserById(@Query("id") int id);

    @GET("/marker/plusPeopleOnEvent")
    public Call<Void> plusPeopleOnEvent(@Query("eventId") float eventId,@Query("userId")int userId);

    @GET("/marker/send")
    public Call<Void> sendMsg(@Query("eventMarker") EventMarker eventMarker);

    //@GET("/placemark/get")
    //public Call<LinkedList<PlaceMark>> getMsg();
}

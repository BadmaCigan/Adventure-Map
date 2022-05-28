package com.example.maps;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {

    //@GET("/java/test")
    //public Call<LinkedList<User>> test(@Query("firstName") String firstName, @Query("lastName") String lastName);
    @GET("/java/test")
    public Call<Void> test(@Query("firstName") String firstName, @Query("lastName") String lastName);

    @GET("/marker/send")
    public Call<Void> sendMsg(@Query("category") int category,
                              @Query("latitude") double latitude,
                              @Query("longitude") double longitude,
                              @Query("description") String description,
                              @Query("title") String title,
                              @Query("address") String address,
                              @Query("hue") float hue,
                              @Query("id") float id,
                              @Query("date") long date,
                              @Query("maxPeople") int maxPeople,
                              @Query("peopleNow") int peopleNow);

    //@GET("/placemark/get")
    //public Call<LinkedList<PlaceMark>> getMsg();
}

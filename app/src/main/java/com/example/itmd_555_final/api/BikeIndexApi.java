package com.example.itmd_555_final.api;

import com.example.itmd_555_final.models.BicycleCollection;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BikeIndexApi {
    @GET("search")
    Call<BicycleCollection> getStolenBikes(
            @Query("location") String location,
            @Query("stolenness") String stolenness,
            @Query("per_page") int perPage,
            @Query("page") int page
    );
}

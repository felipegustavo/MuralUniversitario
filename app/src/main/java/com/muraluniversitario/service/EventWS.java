package com.muraluniversitario.service;

import com.muraluniversitario.model.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventWS {

    @GET("event/get")
    Call<List<Event>> getEvents(@Query("categories") List<String> categories, @Query("institutions") List<String> institutions);

    @GET("event/get/{id}")
    Call<Event> getEvent(@Path("id") String id);

}

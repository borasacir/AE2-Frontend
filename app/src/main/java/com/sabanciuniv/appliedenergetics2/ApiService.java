package com.sabanciuniv.appliedenergetics2;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/screen/login")
    Call<String> login(@Query("username") String username, @Query("password") String password);

    @POST("/screen/signup")
    Call<String> signUp(@Query("username") String username, @Query("password") String password);

    @GET("/api/modpacks")
    Call<List<Modpack>> getModpacks();

    @GET("/api/index/search")
    Call<List<Modpack>> searchModpacks(@Query("query") String query);

    @GET("/api/index/item/{id}")
    Call<Modpack> getModpackDetails(@Query("id") int id);
}

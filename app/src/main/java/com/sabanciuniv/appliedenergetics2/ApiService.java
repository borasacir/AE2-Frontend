package com.sabanciuniv.appliedenergetics2;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/api/modpacks")
    Call<List<Modpack>> getModpacks();

    @GET("/api/index/search")
    Call<List<Modpack>> searchModpacks(@Query("query") String query);

    @GET("/api/index/item/{id}")
    Call<Modpack> getModpackDetails(@Path("id") int id);
}

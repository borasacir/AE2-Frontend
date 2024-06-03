package com.sabanciuniv.appliedenergetics2;

import com.sabanciuniv.appliedenergetics2.Modpack;
import java.util.List;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("modpacks")
    Call<List<Modpack>> getModpacks();

    @GET("modpacks/search")
    Call<List<Modpack>> searchModpacks(@Query("query") String query);

    @GET("modpacks/{id}")
    Call<Modpack> getModpackDetails(@Path("id") int id);
}

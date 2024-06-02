// ApiService.java
package com.sabanciuniv.appliedenergetics2.api;

import com.sabanciuniv.appliedenergetics2.models.Modpack;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("modpacks")
    Call<List<Modpack>> getModpacks();

    @GET("modpacks/search")
    Call<List<Modpack>> searchModpacks(@Query("query") String query);
}

package com.catasoft.ip_finder.data.api;

import com.catasoft.ip_finder.data.entities.SearchInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("{query}")
    Call<SearchInfo> getSearchInfo(@Path("query") String query);


}

package com.example.egida;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonServerAPI {
    @GET("posts")
    Call<List<JsonFile>> getPosts();

    @POST
    Call<ServerPassword> sendPassword();
}
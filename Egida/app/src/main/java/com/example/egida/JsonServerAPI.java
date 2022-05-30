package com.example.egida;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonServerAPI {
    @GET("posts")
    Call<List<JsonFile>> getPosts();

    @GET("images/futurestudio-university-logo.png")
    Call<ResponseBody> downloadFile();
}

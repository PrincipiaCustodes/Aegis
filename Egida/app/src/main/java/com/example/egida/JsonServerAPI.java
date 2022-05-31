package com.example.egida;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface JsonServerAPI {
    @GET("posts")
    Call<List<JsonFile>> getPosts();

    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);
}

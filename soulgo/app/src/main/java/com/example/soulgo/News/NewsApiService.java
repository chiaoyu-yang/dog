package com.example.soulgo.News;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NewsApiService {
    @FormUrlEncoded
    @POST("newsList.php")
    Call<NewsResponseModel> getDataBySort(@Field("sort") Boolean sort);
}

package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("messageList.php")
    Call<PostResponseModel> getDataById(@Field("Nid") String Nid);
}

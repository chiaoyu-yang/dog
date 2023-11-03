package com.example.soulgo.Book;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("detail.php")
    Call<DetailResponseModel> getDataById(@Field("Bid") String Bid);
}

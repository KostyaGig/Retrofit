package com.kostya_zinoviev.retrofit.ex1;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    //Получит все данные
    @GET("posts")
    Call<List<GetPost>> getAllPosts();

//    Отправить даннные
    @POST("posts")
    Call<PostData> postData(@Body PostData postData);

    //Получить даннные по id
    @GET("posts")
    Call<List<GetPost>> getPostById(@Query("userId") Integer userId);

    @PATCH("posts/{id}")
    Call<List<PostData>> getPatch(@Path("id") int id,@Body PostData postData);

    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int id);
}

package com.example.myapplication.data.remote

import com.example.myapplication.data.remote.response.PostsResponse
import com.example.myapplication.domain.model.Post
import retrofit2.http.GET
import retrofit2.http.Query

interface PostApiService {

    @GET("posts")
    suspend fun getPostsPaged(
        @Query("limit") limit: Int = 20,
        @Query("skip") skip: Int = 0
    ): PostsResponse
}
package com.example.myapplication.data.remote

import com.example.myapplication.data.remote.response.PostsResponse
import com.example.myapplication.domain.model.Post
import retrofit2.http.GET

interface PostApiService {

    @GET("posts?limit=20")
    suspend fun getPosts(): PostsResponse
}
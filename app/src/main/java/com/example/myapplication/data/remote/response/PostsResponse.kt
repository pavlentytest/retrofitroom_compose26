package com.example.myapplication.data.remote.response

import com.example.myapplication.domain.model.Post

data class PostsResponse(
    val posts: List<Post>,
    val total: Int,
    val skip: Int,
    val limit: Int
)
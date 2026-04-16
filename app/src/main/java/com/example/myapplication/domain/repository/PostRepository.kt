package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    fun getPostsFlow(): Flow<List<Post>>

    suspend fun refreshPosts(): Result<Unit>

}
package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.model.Post
import com.example.myapplication.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class GetPostsUseCase(
    private val repository: PostRepository
) {
    fun getPostsFlow(): Flow<List<Post>> = repository.getPostsFlow()

    suspend fun refresh(): Result<Unit> = repository.refreshPosts()
}
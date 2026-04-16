package com.example.myapplication.domain.usecase

import androidx.paging.PagingData
import com.example.myapplication.domain.model.Post
import com.example.myapplication.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow


class GetPostsUseCase(
    private val repository: PostRepository
) {
    fun getPostsPaged(): Flow<PagingData<Post>> = repository.getPostsPaged()

    suspend fun refresh(): Result<Unit> = repository.refreshPosts()
}
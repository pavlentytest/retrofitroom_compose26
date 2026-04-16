package com.example.myapplication.domain.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.RemoteMediator
import com.example.myapplication.data.local.PostEntity
import com.example.myapplication.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    fun getPostsPaged(): Flow<PagingData<Post>>

    suspend fun refreshPosts(): Result<Unit>

}
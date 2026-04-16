package com.example.myapplication.data.repository

import android.content.Context
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.myapplication.data.local.PostDatabase
import com.example.myapplication.data.local.PostRemoteMediator
import com.example.myapplication.data.local.toDomain
import com.example.myapplication.data.local.toEntity
import com.example.myapplication.data.remote.RetrofitClient
import com.example.myapplication.domain.model.Post
import com.example.myapplication.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PostRepositoryImpl(context: Context) : PostRepository {

    private val database = PostDatabase.getDatabase(context)
    private val dao = database.postDao()
    private val api = RetrofitClient.apiService

    @OptIn(ExperimentalPagingApi::class)
    override fun getPostsPaged(): Flow<PagingData<Post>> {
        return Pager(
            PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                prefetchDistance = 1,
                enablePlaceholders = false
            ),
            remoteMediator = PostRemoteMediator(api, database),
            pagingSourceFactory = { dao.getPostsPaged() }
        ).flow
            .map { pagingData ->
                pagingData.map { it.toDomain() }
            }
    }

    override suspend fun refreshPosts(): Result<Unit> = runCatching {
        val response = api.getPostsPaged(limit = 50, skip = 0)
        dao.clearAll()
        dao.insertPosts(response.posts.map { it.toEntity() })
    }
}
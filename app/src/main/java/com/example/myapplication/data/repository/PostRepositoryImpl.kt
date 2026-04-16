package com.example.myapplication.data.repository

import android.content.Context
import android.util.Log
import com.example.myapplication.data.local.PostDatabase
import com.example.myapplication.data.local.toDomain
import com.example.myapplication.data.local.toEntity
import com.example.myapplication.data.remote.RetrofitClient
import com.example.myapplication.domain.model.Post
import com.example.myapplication.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PostRepositoryImpl(context: Context) : PostRepository {

    private val dao = PostDatabase.getDatabase(context).postDao()
    private val api = RetrofitClient.apiService

    override fun getPostsFlow(): Flow<List<Post>> {
        return dao.getAllPosts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshPosts(): Result<Unit> = runCatching {
        val response = api.getPosts()
        val freshPosts = response.posts
        dao.clearAll()
        dao.insertPosts(freshPosts.map { it.toEntity() })
    }

}
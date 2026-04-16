package com.example.myapplication.data.local

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.myapplication.data.remote.PostApiService
import com.example.myapplication.data.remote.response.PostsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val api: PostApiService,
    private val database: PostDatabase
) : RemoteMediator<Int, PostEntity>() {

    private val postDao = database.postDao()
    private val remoteKeyDao = database.remoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        return try {
            val skip = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    remoteKeyDao.getNextSkip()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = api.getPostsPaged(
                limit = state.config.pageSize,
                skip = skip
            )

            val posts = response.posts.map { it.toEntity() }
            val endOfPaginationReached = posts.size < state.config.pageSize

            withContext(Dispatchers.IO) {
                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        postDao.clearAll()
                        remoteKeyDao.clearRemoteKeys()
                    }

                    postDao.insertPosts(posts)

                    val nextSkip = skip + posts.size
                    remoteKeyDao.insertRemoteKey(PostRemoteKey(nextSkip = nextSkip))
                }
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
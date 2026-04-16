package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeyDao {
    @Query("SELECT nextSkip FROM remote_keys WHERE id = 0")
    suspend fun getNextSkip(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKey(key: PostRemoteKey)

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}
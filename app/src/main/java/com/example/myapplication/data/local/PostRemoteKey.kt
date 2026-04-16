package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class PostRemoteKey(
    @PrimaryKey val id: Int = 0,
    val nextSkip: Int
)
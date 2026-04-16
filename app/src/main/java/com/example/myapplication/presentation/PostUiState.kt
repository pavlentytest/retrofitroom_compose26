package com.example.myapplication.presentation

import com.example.myapplication.domain.model.Post

sealed class PostUiState {
    object Loading : PostUiState()
    data class Success(val posts: List<Post>) : PostUiState()
    data class Error(val message: String) : PostUiState()
    object Empty : PostUiState()
}
package com.example.myapplication.presentation.ui.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.usecase.GetPostsUseCase
import com.example.myapplication.presentation.PostUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostListViewModel(
    private val getPostsUseCase: GetPostsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PostUiState>(PostUiState.Loading)
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

    init {
        observePosts()
    }

    private fun observePosts() {
        viewModelScope.launch {
            getPostsUseCase.getPostsFlow().collectLatest { posts ->
                _uiState.value = if (posts.isEmpty()) {
                    PostUiState.Empty
                } else {
                    PostUiState.Success(posts)
                }
            }
        }
    }

    fun refreshPosts() {
        viewModelScope.launch {
            Log.d("RRR", "refreshPosts() вызван пользователем")

            _uiState.value = PostUiState.Loading

            try {
                val result = withContext(Dispatchers.IO) {
                    getPostsUseCase.refresh()
                }

                result.onSuccess {
                    Log.d("RRR", "refreshPosts() успешно завершён")
                    // Flow сам обновит состояние
                }.onFailure { e ->
                    Log.e("RRR", "Ошибка при обновлении", e)
                    _uiState.value = PostUiState.Error("Не удалось обновить данные: ${e.message}")
                }
            } catch (e: Exception) {
                Log.e("PostListViewModel", "Неожиданная ошибка", e)
                _uiState.value = PostUiState.Error("Неожиданная ошибка: ${e.message}")
            }
        }
    }
}
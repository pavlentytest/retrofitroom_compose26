package com.example.myapplication.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myapplication.domain.model.Post
import com.example.myapplication.domain.usecase.GetPostsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PostListViewModel(
    private val getPostsUseCase: GetPostsUseCase
) : ViewModel() {

    val postsPagingFlow: Flow<PagingData<Post>> =
        getPostsUseCase.getPostsPaged()
            .cachedIn(viewModelScope)
    
}
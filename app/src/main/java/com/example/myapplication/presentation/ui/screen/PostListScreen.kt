package com.example.myapplication.presentation.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.domain.usecase.GetPostsUseCase
import com.example.myapplication.presentation.PostUiState
import com.example.myapplication.presentation.ui.viewmodel.PostListViewModel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.repository.PostRepositoryImpl
import com.example.myapplication.domain.repository.PostRepository
import com.example.myapplication.presentation.ui.component.ErrorContent
import com.example.myapplication.presentation.ui.component.PostItem

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myapplication.domain.model.Post


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListScreen() {
    val context = LocalContext.current

    val viewModel = remember {
        val repository: PostRepository = PostRepositoryImpl(context)
        val useCase = GetPostsUseCase(repository)
        PostListViewModel(useCase)
    }

    val pagingItems: LazyPagingItems<Post> = viewModel.postsPagingFlow.collectAsLazyPagingItems()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Posts & Paging 3") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(
                count = pagingItems.itemCount,
                key = { pagingItems.peek(it)?.id ?: it }
            ) { index ->
                pagingItems[index]?.let { post ->
                    PostItem(post = post)
                }
            }

            if (pagingItems.loadState.append is LoadState.Loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            if (pagingItems.loadState.append is LoadState.Error) {
                item {
                    Button(onClick = { pagingItems.retry() }) {
                        Text("Повторить загрузку")
                    }
                }
            }
        }
    }
}
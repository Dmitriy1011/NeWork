package ru.testapp.nework.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.testapp.nework.auth.AppAuth
import ru.testapp.nework.dto.Post
import ru.testapp.nework.repository.RepositoryWallMy
import ru.testapp.nework.state.StateFeed
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class ViewModelWallMy @Inject constructor(
    private val repository: RepositoryWallMy,
    appAuth: AppAuth
) : ViewModel() {
    private val cached: Flow<PagingData<Post>> = repository.data.cachedIn(viewModelScope)

    val data: Flow<PagingData<Post>> =
        appAuth
            .authStateFlow
            .flatMapLatest { (myId, _) ->
                cached.map { pagingData ->
                    pagingData.map { item ->
                        if (item !is Post) item else item.copy(ownedByMe = item.authorId == myId)
                    }
                }
            }

    init {
        getMyWallPosts()
    }

    fun getMyWallPosts() {
        viewModelScope.launch {
            try {
                repository.getAllFromMyWall()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

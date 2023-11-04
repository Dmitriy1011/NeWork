package ru.testapp.nework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import ru.testapp.nework.repository.RepositoryWallUser
import ru.testapp.nework.state.StateWallUserPosts
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class ViewModelWallUser @Inject constructor(
    private val repository: RepositoryWallUser,
    appAuth: AppAuth
) : ViewModel() {
    private val cached: Flow<PagingData<Post>> = repository.data.cachedIn(viewModelScope)

    val data: Flow<PagingData<Post>> =
        appAuth
            .authStateFlow
            .flatMapLatest { (myId, _) ->
                cached.map {
                    it.map { item ->
                        if (item is Post) item else item.copy(ownedByMe = item.authorId == myId)
                    }
                }
            }

    private val _wallUserPostsState = MutableLiveData(StateWallUserPosts())
    val wallUserPostsState: LiveData<StateWallUserPosts>
        get() = _wallUserPostsState

    fun loadUserWallPosts(authorId: Long) {
        viewModelScope.launch {
            _wallUserPostsState.value = StateWallUserPosts(loading = true)
            try {
                repository.getAllFromUsersWall(authorId)
                _wallUserPostsState.value = StateWallUserPosts()
            } catch (e: Exception) {
                _wallUserPostsState.value = StateWallUserPosts(error = true)
            }
        }
    }
}
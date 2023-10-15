package ru.testapp.nework.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.testapp.nework.auth.AppAuth
import ru.testapp.nework.dto.Post
import ru.testapp.nework.repository.RepositoryWallUser
import javax.inject.Inject

@HiltViewModel
class ViewModelWallUser @Inject constructor(
    private val repository: RepositoryWallUser,
    private val appAuth: AppAuth
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
}
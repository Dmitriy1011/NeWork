package ru.testapp.nework.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.testapp.nework.dto.Post

interface RepositoryWallMy {
    val data: Flow<PagingData<Post>>
    suspend fun getAllFromMyWall(): List<Post>
}
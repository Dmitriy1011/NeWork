package ru.testapp.nework.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.testapp.nework.dto.Post

interface RepositoryWallUser {
    val data: Flow<PagingData<Post>>
    suspend fun getAllFromUsersWall(authorId: Long): List<Post>
}
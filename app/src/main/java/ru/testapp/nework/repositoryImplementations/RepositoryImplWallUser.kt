package ru.testapp.nework.repositoryImplementations

import android.accounts.NetworkErrorException
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.testapp.nework.api.ApiServiceWallUser
import ru.testapp.nework.dao.DaoPost
import ru.testapp.nework.dao.DaoRemoteKeyWallUsers
import ru.testapp.nework.database.AppDb
import ru.testapp.nework.dto.Post
import ru.testapp.nework.entity.PostEntity
import ru.testapp.nework.paging.RemoteMediatorUserWall
import ru.testapp.nework.repository.RepositoryWallUser
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImplWallUser @Inject constructor(
    private val apiServiceWallUser: ApiServiceWallUser,
    private val appDb: AppDb,
    private val dao: DaoPost,
    private val keyDao: DaoRemoteKeyWallUsers,
) : RepositoryWallUser {

    @OptIn(ExperimentalPagingApi::class)
    override val data: Flow<PagingData<Post>> = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = { dao.getPagingSource() },
        remoteMediator = RemoteMediatorUserWall(
            apiServiceWallUser = apiServiceWallUser,
            appDb = appDb,
            keyDao = keyDao,
        )
    ).flow.map { pagingData ->
        pagingData.map(PostEntity::toDto)
    }

    override suspend fun getAllFromUsersWall(authorId: Long): List<Post> {
        try {
            val response = apiServiceWallUser.getAllFromUserWall(authorId)

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            return response.body() ?: throw RuntimeException("body is null")
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }
}
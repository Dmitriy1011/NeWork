package ru.testapp.nework.repositoryImplementations

import android.accounts.NetworkErrorException
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.testapp.nework.api.ApiServiceWallMy
import ru.testapp.nework.dao.DaoPost
import ru.testapp.nework.dao.DaoPostRemoteKey
import ru.testapp.nework.database.AppDb
import ru.testapp.nework.dto.Post
import ru.testapp.nework.entity.PostEntity
import ru.testapp.nework.paging.RemoteMediatorMyWall
import ru.testapp.nework.repository.RepositoryWallMy
import java.io.IOException
import javax.inject.Singleton

@Singleton
class RepositoryImplWallMy(
    private val apiServiceWallMy: ApiServiceWallMy,
    private val dao: DaoPost,
    private val appDb: AppDb,
    private val keyDao: DaoPostRemoteKey
) : RepositoryWallMy {

    @OptIn(ExperimentalPagingApi::class)
    override val data: Flow<PagingData<Post>> = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = { dao.getPagingSource() },
        remoteMediator = RemoteMediatorMyWall(
            apiServiceWallMy = apiServiceWallMy,
            daoPost = dao,
            appDb = appDb,
            keyDao = keyDao
        )
    ).flow.map {
        it.map(PostEntity::toDto)
    }

    override suspend fun getAllFromMyWall() {
        try {
            val response = apiServiceWallMy.getAllFromMyWall()

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            val body = response.body() ?: throw RuntimeException("body is null")
            dao.insert(body.map(PostEntity::fromDto))
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }
}
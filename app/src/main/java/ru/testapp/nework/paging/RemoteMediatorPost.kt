package ru.testapp.nework.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.netology.nmedia.exceptions.ApiError
import ru.testapp.nework.api.ApiServicePosts
import ru.testapp.nework.dao.DaoPost
import ru.testapp.nework.dao.DaoPostRemoteKey
import ru.testapp.nework.database.AppDb
import ru.testapp.nework.entity.EntityPostRemoteKey
import ru.testapp.nework.entity.PostEntity

@OptIn(ExperimentalPagingApi::class)
class RemoteMediatorPost(
    private val apiService: ApiServicePosts,
    private val appDb: AppDb,
    private val dao: DaoPost,
    private val keyDao: DaoPostRemoteKey
) : RemoteMediator<Int, PostEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        try {
            val response = when (loadType) {
                LoadType.APPEND -> {
                    val id =
                        keyDao.max() ?: return RemoteMediator.MediatorResult.Success(
                            false
                        )
                    apiService.getAfter(id, state.config.pageSize)
                }

                LoadType.PREPEND -> return MediatorResult.Success(true)

                LoadType.REFRESH -> apiService.getLatestPosts(state.config.pageSize)
            }

            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(
                response.code(),
                response.message()
            )

            appDb.withTransaction {
                when (loadType) {
                    LoadType.REFRESH -> {
                        if (keyDao.isEmpty()) {
                            keyDao.insert(
                                listOf(
                                    EntityPostRemoteKey(
                                        EntityPostRemoteKey.KeyType.AFTER,
                                        body.first().id
                                    ),
                                    EntityPostRemoteKey(
                                        EntityPostRemoteKey.KeyType.BEFORE,
                                        body.last().id
                                    )
                                )
                            )
                        } else {
                            keyDao.insert(
                                EntityPostRemoteKey(
                                    EntityPostRemoteKey.KeyType.AFTER,
                                    body.first().id
                                ),
                            )
                        }
                    }

                    LoadType.APPEND -> {
                        EntityPostRemoteKey(
                            EntityPostRemoteKey.KeyType.BEFORE,
                            body.last().id
                        )
                    }

                    else -> Unit
                }
                dao.insert(body.map(PostEntity::fromDto))
            }

            return MediatorResult.Success(
                body.isEmpty()
            )

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}
package ru.testapp.nework.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.netology.nmedia.exceptions.ApiError
import ru.testapp.nework.api.ApiServiceWallMy
import ru.testapp.nework.dao.DaoPost
import ru.testapp.nework.dao.DaoRemoteKeyWallMy
import ru.testapp.nework.database.AppDb
import ru.testapp.nework.entity.EntityRemoteKeyWallMy
import ru.testapp.nework.entity.PostEntity

@OptIn(ExperimentalPagingApi::class)
class RemoteMediatorMyWall(
    private val apiServiceWallMy: ApiServiceWallMy,
    private val appDb: AppDb,
    private val keyDao: DaoRemoteKeyWallMy
) : RemoteMediator<Int, PostEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        try {
            val response = when (loadType) {
                LoadType.REFRESH -> apiServiceWallMy.getLatestFromMyWall(state.config.pageSize)
                LoadType.PREPEND -> return MediatorResult.Success(false)
                LoadType.APPEND -> {
                    val id =
                        keyDao.max() ?: return RemoteMediator.MediatorResult.Success(false)
                    apiServiceWallMy.getAfterFromMyWall(id, state.config.pageSize)
                }
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
                                    EntityRemoteKeyWallMy(
                                        EntityRemoteKeyWallMy.KeyType.AFTER,
                                        body.first().id
                                    ),
                                    EntityRemoteKeyWallMy(
                                        EntityRemoteKeyWallMy.KeyType.BEFORE,
                                        body.last().id
                                    )
                                )
                            )
                        } else {
                            keyDao.insert(
                                EntityRemoteKeyWallMy(
                                    EntityRemoteKeyWallMy.KeyType.AFTER,
                                    body.first().id
                                ),
                            )
                        }
                    }

                    LoadType.APPEND -> {
                        EntityRemoteKeyWallMy(
                            EntityRemoteKeyWallMy.KeyType.BEFORE,
                            body.last().id
                        )
                    }

                    else -> Unit
                }
            }
            return MediatorResult.Success(
                body.isEmpty()
            )
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}
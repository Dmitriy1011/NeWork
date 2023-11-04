package ru.testapp.nework.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.netology.nmedia.exceptions.ApiError
import ru.testapp.nework.api.ApiServiceWallUser
import ru.testapp.nework.dao.DaoRemoteKeyWallUsers
import ru.testapp.nework.database.AppDb
import ru.testapp.nework.dto.Post
import ru.testapp.nework.entity.EntityRemoteKeyWallUser
import ru.testapp.nework.entity.PostEntity

@OptIn(ExperimentalPagingApi::class)
class RemoteMediatorUserWall(
    private val apiServiceWallUser: ApiServiceWallUser,
    private val appDb: AppDb,
    private val keyDao: DaoRemoteKeyWallUsers,
) : RemoteMediator<Int, PostEntity>() {

    val post: Post? = null
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        try {
            val response = when (loadType) {
                LoadType.REFRESH -> apiServiceWallUser.getLatestFromUserWall(
                    post?.authorId!!,
                    state.config.pageSize
                )

                LoadType.PREPEND -> return RemoteMediator.MediatorResult.Success(false)
                LoadType.APPEND -> {
                    val id = keyDao.max() ?: return RemoteMediator.MediatorResult.Success(false)
                    apiServiceWallUser.getAfterFromUserWall(
                        post?.authorId!!,
                        id,
                        state.config.pageSize
                    )
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
                                    EntityRemoteKeyWallUser(
                                        EntityRemoteKeyWallUser.KeyType.AFTER,
                                        body.first().id
                                    ),
                                    EntityRemoteKeyWallUser(
                                        EntityRemoteKeyWallUser.KeyType.BEFORE,
                                        body.last().id
                                    )
                                )
                            )
                        } else {
                            EntityRemoteKeyWallUser(
                                EntityRemoteKeyWallUser.KeyType.AFTER,
                                body.first().id
                            )
                        }
                    }

                    LoadType.APPEND -> {
                        EntityRemoteKeyWallUser(
                            EntityRemoteKeyWallUser.KeyType.BEFORE,
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
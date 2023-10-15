package ru.testapp.nework.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.netology.nmedia.exceptions.ApiError
import ru.testapp.nework.api.ApiServiceWallUser
import ru.testapp.nework.dao.DaoPost
import ru.testapp.nework.dao.DaoPostRemoteKey
import ru.testapp.nework.database.AppDb
import ru.testapp.nework.dto.Post
import ru.testapp.nework.entity.EntityPostRemoteKey
import ru.testapp.nework.entity.PostEntity

@OptIn(ExperimentalPagingApi::class)
class RemoteMediatorUserWall(
    private val apiServiceWallUser: ApiServiceWallUser,
    private val appDb: AppDb,
    private val daoPost: DaoPost,
    private val keyDao: DaoPostRemoteKey,
    private val post: Post
) : RemoteMediator<Int, PostEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        try {
            val response = when (loadType) {
                LoadType.REFRESH -> apiServiceWallUser.getLatestFromUserWall(
                    post.authorId,
                    state.config.pageSize
                )

                LoadType.PREPEND -> return RemoteMediator.MediatorResult.Success(false)
                LoadType.APPEND -> {
                    val id = keyDao.max() ?: return RemoteMediator.MediatorResult.Success(false)
                    apiServiceWallUser.getAfterFromUserWall(
                        post.authorId,
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
                            EntityPostRemoteKey(
                                EntityPostRemoteKey.KeyType.AFTER,
                                body.first().id
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
                daoPost.insert(body.map(PostEntity::fromDto))
            }
            return MediatorResult.Success(
                body.isEmpty()
            )
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}
package ru.testapp.nework.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.netology.nmedia.exceptions.ApiError
import ru.testapp.nework.api.ApiServiceEvents
import ru.testapp.nework.dao.DaoEvent
import ru.testapp.nework.dao.DaoEventRemoteKey
import ru.testapp.nework.database.AppDb
import ru.testapp.nework.entity.EntityEvent
import ru.testapp.nework.entity.EntityEventRemoteKey

@OptIn(ExperimentalPagingApi::class)
class RemoteMediatorEvent(
    private val apiService: ApiServiceEvents,
    private val appDb: AppDb,
    private val dao: DaoEvent,
    private val keyDao: DaoEventRemoteKey
) : RemoteMediator<Int, EntityEvent>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EntityEvent>
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

                LoadType.REFRESH -> apiService.getLatestEvents(state.config.pageSize)
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
                                    EntityEventRemoteKey(
                                        EntityEventRemoteKey.KeyType.AFTER,
                                        body.first().id
                                    ),
                                    EntityEventRemoteKey(
                                        EntityEventRemoteKey.KeyType.BEFORE,
                                        body.last().id
                                    )
                                )
                            )
                        } else {
                            keyDao.insert(
                                EntityEventRemoteKey(
                                    EntityEventRemoteKey.KeyType.AFTER,
                                    body.first().id
                                ),
                            )
                        }
                    }

                    LoadType.APPEND -> {
                        EntityEventRemoteKey(
                            EntityEventRemoteKey.KeyType.BEFORE,
                            body.last().id
                        )
                    }

                    else -> Unit
                }
                dao.insert(body.map(EntityEvent::fromDto))
            }

            return MediatorResult.Success(
                body.isEmpty()
            )

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}

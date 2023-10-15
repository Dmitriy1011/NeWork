package ru.testapp.nework.repositoryImplementations

import android.accounts.NetworkErrorException
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.testapp.nework.api.ApiServiceEvents
import ru.testapp.nework.dao.DaoEvent
import ru.testapp.nework.dao.DaoEventRemoteKey
import ru.testapp.nework.database.AppDb
import ru.testapp.nework.dto.AttachmentType
import ru.testapp.nework.dto.Event
import ru.testapp.nework.dto.Media
import ru.testapp.nework.entity.EntityEvent
import ru.testapp.nework.entity.EventAttachmentEmbeddable
import ru.testapp.nework.paging.RemoteMediatorEvent
import ru.testapp.nework.repository.RepositoryEvents
import java.io.File
import java.io.IOException
import javax.inject.Singleton

@Singleton
class RepositoryImplEvent(
    private val apiService: ApiServiceEvents,
    private val eventDao: DaoEvent,
    private val keyDao: DaoEventRemoteKey,
    private val appDb: AppDb
) : RepositoryEvents {

    @OptIn(ExperimentalPagingApi::class)
    override val data: Flow<PagingData<Event>> = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = { eventDao.getPagingSource() },
        remoteMediator = RemoteMediatorEvent(
            apiService = apiService,
            dao = eventDao,
            keyDao = keyDao,
            appDb = appDb
        )
    ).flow.map {
        it.map(EntityEvent::toDto)
    }

    override suspend fun getAllEvents() {
        try {
            val response = apiService.getAllEvents()

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            val result = response.body() ?: throw RuntimeException(response.message())
            eventDao.insert(result.map(EntityEvent::fromDto))
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }

    override suspend fun saveEvent(event: Event) {
        try {
            val response = apiService.saveEvent(event)

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            val result = response.body() ?: throw RuntimeException(response.message())
            eventDao.insert(EntityEvent.fromDto(result))
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }

    override suspend fun editEvent(event: Event) {
        try {
            val response = apiService.editEvent(event)

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            val result = response.body() ?: throw RuntimeException(response.message())
            eventDao.insert(EntityEvent.fromDto(result))
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }

    override suspend fun removeEvent(id: Long) {
        try {
            val response = apiService.deleteEvent(id)

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            eventDao.removeById(id)
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }

    override suspend fun like(id: Long): Event {
        try {
            eventDao.handleLike(id)

            val response = apiService.likeEvent(id)

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            return response.body() ?: throw RuntimeException("body is null")
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }

    override suspend fun unLike(id: Long): Event {
        try {
            eventDao.handleLike(id)

            val response = apiService.unLikeEvent(id)

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            return response.body() ?: throw RuntimeException("body is null")
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }

    private suspend fun uploadMedia(file: File): Media {
        val formData = MultipartBody.Part.createFormData(
            "file", file.name, file.asRequestBody()
        )

        val response = apiService.uploadMedia(formData)

        if (!response.isSuccessful) {
            throw RuntimeException(response.message())
        }

        return response.body() ?: throw RuntimeException()
    }


    override suspend fun saveEventWithAttachment(event: Event, file: File) {
        try {
            val media = uploadMedia(file)

            val response = apiService.saveEvent(
                event.copy(
                    attachment = EventAttachmentEmbeddable(
                        url = media.id,
                        AttachmentType.IMAGE,
                    )
                )
            )
            val result = response.body() ?: throw RuntimeException("body is null")
            eventDao.insert(EntityEvent.fromDto(result))

        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }
}
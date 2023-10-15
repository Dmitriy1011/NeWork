package ru.testapp.nework.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.testapp.nework.dto.Event
import java.io.File

interface RepositoryEvents {
    val data: Flow<PagingData<Event>>
    suspend fun getAllEvents()
    suspend fun saveEvent(event: Event)
    suspend fun editEvent(event: Event)
    suspend fun removeEvent(id: Long)
    suspend fun like(id: Long): Event
    suspend fun unLike(id: Long): Event
    suspend fun saveEventWithAttachment(event: Event, file: File)
}
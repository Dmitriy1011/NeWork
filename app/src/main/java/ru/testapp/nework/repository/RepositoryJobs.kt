package ru.testapp.nework.repository

import kotlinx.coroutines.flow.Flow
import ru.testapp.nework.dto.Job

interface RepositoryJobs {
    val dataMyJobs: Flow<List<Job>>
    val dataUserJobs: Flow<List<Job>>
    suspend fun getMyJobs()
    suspend fun getUsersJobs(id: Long)
    suspend fun saveJob(job: Job)
    suspend fun editJob(job: Job)
    suspend fun removeJob(id: Long)
}
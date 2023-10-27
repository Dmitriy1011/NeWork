package ru.testapp.nework.repositoryImplementations

import android.accounts.NetworkErrorException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.testapp.nework.api.ApiServiceJobs
import ru.testapp.nework.dao.DaoJob
import ru.testapp.nework.dto.Job
import ru.testapp.nework.entity.EntityJob
import ru.testapp.nework.entity.listToDto
import ru.testapp.nework.repository.RepositoryJobs
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImplJob @Inject constructor(
    private val apiServiceJobs: ApiServiceJobs,
    private val dao: DaoJob
) : RepositoryJobs {
    override val dataMyJobs: Flow<List<Job>> =
        dao.getMyJobs().map(List<EntityJob>::listToDto).flowOn(Dispatchers.Default)
    override val dataUserJobs: Flow<List<Job>> =
        dao.getMyJobs().map(List<EntityJob>::listToDto).flowOn(Dispatchers.Default)

    override suspend fun getMyJobs() {
        try {
            val response = apiServiceJobs.getMyJobs()

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            val result = response.body() ?: throw RuntimeException("body is null")
            dao.insert(result.map(EntityJob::fromDto))
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }

    override suspend fun getUsersJobs(id: Long) {
        try {
            val response = apiServiceJobs.getUsersJobs(id)

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            val result = response.body() ?: throw RuntimeException("body is null")
            dao.insert(result.map(EntityJob::fromDto))
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }

    override suspend fun saveJob(job: Job) {
        try {
            val response = apiServiceJobs.saveJob(job)

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            val result = response.body() ?: throw RuntimeException("body is null")
            dao.insert(EntityJob.fromDto(result))
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }

    override suspend fun editJob(job: Job) {
        try {
            val response = apiServiceJobs.editJob(job)

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            val result = response.body() ?: throw RuntimeException("body is null")
            dao.insert(EntityJob.fromDto(result))
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }

    override suspend fun removeJob(id: Long) {
        try {
            val response = apiServiceJobs.removeJob(id)

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            dao.removeJob(id)
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }
}
package ru.testapp.nework.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import ru.testapp.nework.dto.Job

interface ApiServiceJobs {
    @GET("/api/jobs/")
    suspend fun getMyJobs(): Response<List<Job>>

    @GET("/api/{user_id}/jobs/")
    suspend fun getUsersJobs(@Path("user_id") id: Long): Response<List<Job>>

    @POST("/api/jobs/")
    suspend fun saveJob(@Body job: Job): Response<Job>

    @PATCH("/api/jobs/")
    suspend fun editJob(@Body job: Job): Response<Job>

    @DELETE("/api/jobs/{id}/")
    suspend fun removeJob(@Path("id") id: Long): Response<Unit>
}
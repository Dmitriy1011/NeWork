package ru.testapp.nework.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.testapp.nework.dto.Post

interface ApiServiceWallMy {
    @GET("my/wall")
    suspend fun getAllFromMyWall(): Response<List<Post>>

    @GET("my/wall/latest")
    suspend fun getLatestFromMyWall(@Query("count") count: Int): Response<List<Post>>

    @GET("my/wall/{post_id}/after")
    suspend fun getBeforeFromMyWall(
        @Path("post_id") id: Long,
        @Query("count") count: Int
    ): Response<List<Post>>

    @GET("my/wall/{post_id}/after")
    suspend fun getAfterFromMyWall(
        @Path("post_id") id: Long,
        @Query("count") count: Int
    ): Response<List<Post>>
}
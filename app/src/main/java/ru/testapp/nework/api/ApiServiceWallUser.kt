package ru.testapp.nework.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.testapp.nework.dto.Post

interface ApiServiceWallUser {
    @GET("{author_id}/wall")
    suspend fun getAllFromUserWall(@Path("author_id") id: Long): Response<List<Post>>

    @GET("{author_id}/wall/latest")
    suspend fun getLatestFromUserWall(
        @Path("author_id") id: Long,
        @Query("count") count: Int
    ): Response<List<Post>>

    @GET("{author_id}/wall/{post_id}/after")
    suspend fun getBeforeFromUserWall(
        @Path("author_id") authorId: Long,
        @Path("post_id") postId: Long,
        @Query("count") count: Int
    ): Response<List<Post>>

    @GET("{author_id}/wall/{post_id}/before")
    suspend fun getAfterFromUserWall(
        @Path("author_id") authorId: Long,
        @Path("post_id") postId: Long,
        @Query("count") count: Int
    ): Response<List<Post>>
}
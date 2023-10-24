package ru.testapp.nework.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import ru.testapp.nework.dto.Media
import ru.testapp.nework.dto.Post


interface ApiServicePosts {
    @GET("/api/posts/")
    suspend fun getAllPosts(): Response<List<Post>>

    @GET("/api/posts/latest/")
    suspend fun getLatestPosts(@Query("count") count: Int): Response<List<Post>>

    @GET("/api/posts/{post_id}/before/")
    suspend fun getBefore(@Path("post_id") id: Long, @Query("count") count: Int): Response<List<Post>>

    @GET("/api/posts/{post_id}/after/")
    suspend fun getAfter(@Path("post_id") id: Long, @Query("count") count: Int): Response<List<Post>>

    @POST("/api/posts/")
    suspend fun savePost(@Body post: Post): Response<Post>

    @POST("/api/posts/{post_id}/")
    suspend fun likePost(@Path("post_id") id: Long): Response<Post>

    @POST("/api/posts/{post_id}/")
    suspend fun unLikePost(@Path("post_id") id: Long): Response<Post>

    @DELETE("/api/posts/{post_id}/")
    suspend fun deletePost(@Path("post_id") id: Long): Response<Unit>

    @PATCH("/api/posts/")
    suspend fun editPost(@Body post: Post): Response<Post>

    @Multipart
    @POST("/api/media/")
    suspend fun uploadMedia(@Part file: MultipartBody.Part): Response<Media>
}
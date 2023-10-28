package ru.testapp.nework.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import ru.testapp.nework.dto.Token
import ru.testapp.nework.dto.User

interface ApiServiceUsers {
    @GET("/api/users/")
    suspend fun getAllUsers(): Response<List<User>>

    @GET("/api/users/{user_id}/")
    suspend fun getUserById(@Path("user_id") id: Long): Response<User>

    @FormUrlEncoded
    @POST("/api/users/registration/")
    suspend fun registerUser(
        @Part("login") login: RequestBody,
        @Part("password") password: RequestBody,
        @Part("name") name: RequestBody,
        @Part media: MultipartBody.Part
    ): Response<Token>

    @FormUrlEncoded
    @POST("/api/users/registration/")
    suspend fun registerUserWithoutAvatar(
        @Part("login") login: RequestBody,
        @Part("password") password: RequestBody,
        @Part("name") name: RequestBody
    ): Response<Token>

    @FormUrlEncoded
    @POST("/api/users/authentication/")
    suspend fun updateUser(
        @Field("login") login: String,
        @Field("password") password: String
    ): Response<Token>
}
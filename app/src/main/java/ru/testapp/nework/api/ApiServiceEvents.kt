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
import ru.testapp.nework.dto.Event
import ru.testapp.nework.dto.Media

interface ApiServiceEvents {
    @GET("/api/events/")
    suspend fun getAllEvents(): Response<List<Event>>

    @GET("/api/events/latest/")
    suspend fun getLatestEvents(@Query("count") count: Int): Response<List<Event>>

    @GET("/api/events/{event_id}/before/")
    suspend fun getBefore(
        @Path("event_id") id: Long,
        @Query("count") count: Int
    ): Response<List<Event>>

    @GET("/api/events/{event_id}/after/")
    suspend fun getAfter(
        @Path("event_id") id: Long,
        @Query("count") count: Int
    ): Response<List<Event>>

    @POST("/api/events/")
    suspend fun saveEvent(@Body event: Event): Response<Event>

    @POST("/api/events/{event_id}/")
    suspend fun likeEvent(@Path("event_id") id: Long): Response<Event>

    @POST("/api/events/{event_id}/")
    suspend fun unLikeEvent(@Path("event_id") id: Long): Response<Event>

    @DELETE("/api/events/{event_id}/")
    suspend fun deleteEvent(@Path("event_id") id: Long): Response<Unit>

    @PATCH("/api/events/")
    suspend fun editEvent(@Body event: Event): Response<Event>

    @Multipart
    @POST("/api/media/")
    suspend fun uploadMedia(@Part file: MultipartBody.Part): Response<Media>
}
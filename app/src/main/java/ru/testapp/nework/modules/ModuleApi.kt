package ru.testapp.nework.modules

import com.google.firebase.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.testapp.nework.api.ApiServiceEvents
import ru.testapp.nework.api.ApiServiceJobs
import ru.testapp.nework.api.ApiServicePosts
import ru.testapp.nework.api.ApiServiceUsers
import ru.testapp.nework.api.ApiServiceWallMy
import ru.testapp.nework.api.ApiServiceWallUser
import ru.testapp.nework.auth.AppAuth
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ModuleApi {

    @Singleton
    @Provides
    fun provideLogging(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }


    @Singleton
    @Provides
    fun provideOkHttp(
        appAuth: AppAuth
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .addInterceptor { chain ->
                appAuth.authStateFlow.value.token?.let { token ->
                    val newRequest = chain.request().newBuilder()
                        .addHeader(
                            "Authorization",
                            token
                        )
                        .build()
                    return@addInterceptor chain.proceed(newRequest)
                }
                chain.proceed(chain.request())
            }
            .build()


    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient) =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
            .build()

    @Singleton
    @Provides
    fun providePostsApiService(
        retrofit: Retrofit
    ): ApiServicePosts = retrofit.create<ApiServicePosts>()

    @Singleton
    @Provides
    fun provideUsersApiService(
        retrofit: Retrofit
    ): ApiServiceUsers = retrofit.create<ApiServiceUsers>()

    @Singleton
    @Provides
    fun provideEventsApiService(
        retrofit: Retrofit
    ): ApiServiceEvents = retrofit.create<ApiServiceEvents>()

    @Singleton
    @Provides
    fun provideJobsApiService(
        retrofit: Retrofit
    ): ApiServiceJobs = retrofit.create<ApiServiceJobs>()

    @Singleton
    @Provides
    fun provideWallMyApiService(
        retrofit: Retrofit
    ): ApiServiceWallMy = retrofit.create<ApiServiceWallMy>()

    @Singleton
    @Provides
    fun provideWallUserApiService(
        retrofit: Retrofit
    ): ApiServiceWallUser = retrofit.create<ApiServiceWallUser>()

    companion object {
        const val BASE_URL = "https://netomedia.ru/"
    }
}
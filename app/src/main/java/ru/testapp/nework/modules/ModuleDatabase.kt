package ru.testapp.nework.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.testapp.nework.dao.DaoEvent
import ru.testapp.nework.dao.DaoEventRemoteKey
import ru.testapp.nework.dao.DaoJob
import ru.testapp.nework.dao.DaoPost
import ru.testapp.nework.dao.DaoPostRemoteKey
import ru.testapp.nework.dao.DaoRemoteKeyWallMy
import ru.testapp.nework.dao.DaoRemoteKeyWallUsers
import ru.testapp.nework.dao.DaoUser
import ru.testapp.nework.database.AppDb
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ModuleDatabase {

    @Singleton
    @Provides
    fun provideAppDb(
        @ApplicationContext
        context: Context
    ): AppDb =
        Room.databaseBuilder(context, AppDb::class.java, "app.db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun providePostDao(appDb: AppDb): DaoPost = appDb.postDao()

    @Singleton
    @Provides
    fun providePostRemoteKeyDao(appDb: AppDb): DaoPostRemoteKey = appDb.postRemoteKeyDao()

    @Singleton
    @Provides
    fun provideEventDao(appDb: AppDb): DaoEvent = appDb.eventDao()

    @Singleton
    @Provides
    fun provideEventRemoteKeyDao(appDb: AppDb): DaoEventRemoteKey = appDb.eventRemoteKeyDao()

    @Singleton
    @Provides
    fun provideDaoUser(appDb: AppDb): DaoUser = appDb.userDao()

    @Singleton
    @Provides
    fun provideDaoJob(appDb: AppDb): DaoJob = appDb.jobDao()

    @Singleton
    @Provides
    fun provideDaoRemoteKeyWallMy(appDb: AppDb): DaoRemoteKeyWallMy = appDb.remoteKeyWallMyDao()

    @Singleton
    @Provides
    fun provideDaoRemoteKeyWallUser(appDb: AppDb): DaoRemoteKeyWallUsers =
        appDb.remoteKeyWallUserDao()
}
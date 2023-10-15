package ru.testapp.nework.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.testapp.nework.dao.DaoPost
import ru.testapp.nework.dao.DaoPostRemoteKey
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
}
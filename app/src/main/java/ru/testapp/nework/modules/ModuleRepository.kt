package ru.testapp.nework.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.testapp.nework.repository.RepositoryEvents
import ru.testapp.nework.repository.RepositoryJobs
import ru.testapp.nework.repository.RepositoryPost
import ru.testapp.nework.repository.RepositoryUsers
import ru.testapp.nework.repository.RepositoryWallMy
import ru.testapp.nework.repository.RepositoryWallUser
import ru.testapp.nework.repositoryImplementations.RepositoryImplEvent
import ru.testapp.nework.repositoryImplementations.RepositoryImplJob
import ru.testapp.nework.repositoryImplementations.RepositoryImplPost
import ru.testapp.nework.repositoryImplementations.RepositoryImplUser
import ru.testapp.nework.repositoryImplementations.RepositoryImplWallMy
import ru.testapp.nework.repositoryImplementations.RepositoryImplWallUser
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface ModuleRepository {
    @Binds
    @Singleton
    fun bindPostsRepository(
        repositoryImplPost: RepositoryImplPost
    ): RepositoryPost

    @Binds
    @Singleton
    fun bindUsersRepository(
        repositoryImplUser: RepositoryImplUser
    ): RepositoryUsers

    @Binds
    @Singleton
    fun bindJobsRepository(
        repositoryImplJob: RepositoryImplJob
    ): RepositoryJobs

    @Binds
    @Singleton
    fun bindEventsRepository(
        repositoryImplEvent: RepositoryImplEvent
    ): RepositoryEvents

    @Binds
    @Singleton
    fun bindWallMyRepository(
        repositoryImplWallMy: RepositoryImplWallMy
    ): RepositoryWallMy

    @Binds
    @Singleton
    fun bindWallUserRepository(
        repositoryImplWallUser: RepositoryImplWallUser
    ): RepositoryWallUser
}
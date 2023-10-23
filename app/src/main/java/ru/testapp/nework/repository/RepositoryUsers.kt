package ru.testapp.nework.repository

import kotlinx.coroutines.flow.Flow
import ru.testapp.nework.dto.User
import java.io.File

interface RepositoryUsers {
    val data: Flow<List<User>>
    suspend fun getAllUsers()
    suspend fun getUserById(id: Long): User
    suspend fun setIdAndTokenToAuth(id: String, token: String)
    suspend fun registerUser(login: String, name: String, password: String, file: File)
}
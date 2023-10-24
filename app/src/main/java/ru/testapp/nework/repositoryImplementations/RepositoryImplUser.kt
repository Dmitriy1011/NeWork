package ru.testapp.nework.repositoryImplementations

import android.accounts.NetworkErrorException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.testapp.nework.api.ApiServiceUsers
import ru.testapp.nework.auth.AppAuth
import ru.testapp.nework.dao.DaoUser
import ru.testapp.nework.dto.User
import ru.testapp.nework.entity.EntityUser
import ru.testapp.nework.entity.toDto
import ru.testapp.nework.repository.RepositoryUsers
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImplUser @Inject constructor(
    private val apiService: ApiServiceUsers,
    private val dao: DaoUser
) : RepositoryUsers {

    override val data =
        dao.getAllUsers().map(List<EntityUser>::toDto).flowOn(Dispatchers.Default)

    @Inject
    lateinit var appAuth: AppAuth

    override suspend fun getAllUsers() {
        try {
            val response = apiService.getAllUsers()

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            val result = response.body() ?: throw RuntimeException(response.message())
            dao.insert(result.map(EntityUser::fromDto))
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }

    override suspend fun setIdAndTokenToAuth(id: String, token: String) {
        try {
            val response = apiService.updateUser(id, token)

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            val result = response.body() ?: throw RuntimeException("body is null")
            appAuth.setAuth(result.id, result.token)
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }


    override suspend fun registerUser(
        login: String,
        name: String,
        password: String,
        file: File
    ) {

        try {
            val formData = MultipartBody.Part.createFormData(
                "file", file.name, file.asRequestBody()
            )

            val userLogin = login.toRequestBody("text/plain".toMediaType())
            val userPassword = password.toRequestBody("text/plain".toMediaType())
            val userName = name.toRequestBody("text/plain".toMediaType())

            val response = apiService.registerUser(userLogin, userPassword, userName, formData)

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            val result = response.body() ?: throw RuntimeException("body is null")
            appAuth.setAuth(result.id, result.token)
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }

    override suspend fun getUserById(id: Long): User {
        try {
            val response = apiService.getUserById(id)

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            val result = response.body()
            return result!!
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }
}
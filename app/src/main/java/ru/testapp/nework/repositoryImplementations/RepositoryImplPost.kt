package ru.testapp.nework.repositoryImplementations

import android.accounts.NetworkErrorException
import androidx.lifecycle.asLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.Dispatcher
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.testapp.nework.api.ApiServicePosts
import ru.testapp.nework.dao.DaoPost
import ru.testapp.nework.dao.DaoPostRemoteKey
import ru.testapp.nework.database.AppDb
import ru.testapp.nework.dto.Media
import ru.testapp.nework.dto.Post
import ru.testapp.nework.entity.AttachmentEmbeddable
import ru.testapp.nework.entity.PostEntity
import ru.testapp.nework.entity.listToDto
import ru.testapp.nework.enum.AttachmentTypePost
import ru.testapp.nework.paging.RemoteMediatorPost
import ru.testapp.nework.repository.RepositoryPost
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImplPost @Inject constructor(
    private val apiService: ApiServicePosts,
    private val postDao: DaoPost,
    private val keyDao: DaoPostRemoteKey,
    private val appDb: AppDb,
) : RepositoryPost {

    @OptIn(ExperimentalPagingApi::class)
    override val data: Flow<PagingData<Post>> = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = { postDao.getPagingSource() },
        remoteMediator = RemoteMediatorPost(
            apiService = apiService,
            dao = postDao,
            keyDao = keyDao,
            appDb = appDb
        )
    ).flow.map {
        it.map(PostEntity::toDto)
    }

    override val postData: Flow<List<Post>> = postDao.getAllPosts().map(List<PostEntity>::listToDto).flowOn(Dispatchers.Default)

    override suspend fun getAllPosts() {
        try {
            val response = apiService.getAllPosts()

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            val result = response.body() ?: throw RuntimeException(response.message())
            postDao.insert(result.map(PostEntity::fromDto))
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }

    override suspend fun savePost(post: Post) {
        try {
            val response = apiService.savePost(post)

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            val result = response.body() ?: throw RuntimeException(response.message())
            postDao.insert(PostEntity.fromDto(result))
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }

    override suspend fun editPost(post: Post) {
        try {
            val response = apiService.editPost(post)

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            val result = response.body() ?: throw RuntimeException(response.message())
            postDao.insert(PostEntity.fromDto(result))
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }

    override suspend fun removePost(id: Long) {
        try {
            val response = apiService.deletePost(id)

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            postDao.removeById(id)
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }

    override suspend fun like(id: Long): Post {
        try {
            postDao.handleLike(id)

            val response = apiService.likePost(id)

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            return response.body() ?: throw RuntimeException("body is null")
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }

    override suspend fun unLike(id: Long): Post {
        try {
            postDao.handleLike(id)

            val response = apiService.unLikePost(id)

            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }

            return response.body() ?: throw RuntimeException("body is null")
        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }

    private suspend fun uploadMedia(file: File): Media {
        val formData = MultipartBody.Part.createFormData(
            "file", file.name, file.asRequestBody()
        )

        val response = apiService.uploadMedia(formData)

        if (!response.isSuccessful) {
            throw RuntimeException(response.message())
        }

        return response.body() ?: throw RuntimeException()
    }


    override suspend fun savePostWithAttachment(post: Post, file: File) {
        try {
            val media = uploadMedia(file)

            val response = apiService.savePost(
                post.copy(
                    attachment = AttachmentEmbeddable(
                        url = media.id,
                        AttachmentTypePost.IMAGE.toString(),
                    )
                )
            )
            val result = response.body() ?: throw RuntimeException("body is null")
            postDao.insert(PostEntity.fromDto(result))

        } catch (e: IOException) {
            throw NetworkErrorException(e)
        }
    }
}
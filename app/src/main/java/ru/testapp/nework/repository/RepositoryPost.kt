package ru.testapp.nework.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.testapp.nework.dto.Post
import java.io.File

interface RepositoryPost {
    val data: Flow<PagingData<Post>>
    suspend fun getAllPosts()
    suspend fun savePost(post: Post)
    suspend fun editPost(post: Post)
    suspend fun removePost(id: Long)
    suspend fun like(id: Long): Post
    suspend fun unLike(id: Long): Post
    suspend fun savePostWithAttachment(post: Post, file: File)
}
package ru.testapp.nework.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.testapp.nework.entity.PostEntity

@Dao
interface DaoPost {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM PostEntity WHERE id=:id")
    fun getPost(id: Long): PostEntity

    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getPagingSource(): PagingSource<Int, PostEntity>

    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun pagingSource(): PagingSource<Int, PostEntity>

    @Query("SELECT COUNT(*) == 0 FROM PostEntity")
    suspend fun isEmpty(): Boolean

    @Query("SELECT * FROM PostEntity ORDER BY id DESC LIMIT :count")
    fun getLatestPosts(count: Int = 1): Flow<List<PostEntity>>

    @Query("UPDATE PostEntity SET content = :content WHERE id=:id")
    suspend fun updateContentById(id: Long, content: String)

    suspend fun savePost(postEntity: PostEntity) =
        if (postEntity.id == 0L) insert(postEntity) else updateContentById(
            postEntity.id,
            postEntity.content
        )


    @Query("""
            UPDATE PostEntity SET
            likes = likes + CASE WHEN likedByMe THEN -1 ELSE + 1 END,
            likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
            WHERE id = :id
        """
    )
    suspend fun handleLike(id: Long)


    @Query("DELETE FROM PostEntity WHERE id=:id")
    suspend fun removeById(id: Long)

    @Query("DELETE FROM PostEntity")
    fun clear()

    @Insert
    suspend fun insert(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostEntity>)
}
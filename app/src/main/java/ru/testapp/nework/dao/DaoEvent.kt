package ru.testapp.nework.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.testapp.nework.entity.EntityEvent

@Dao
interface DaoEvent {
    @Query("SELECT * FROM EntityEvent ORDER BY id DESC")
    fun getAllPosts(): Flow<List<EntityEvent>>

    @Query("SELECT * FROM EntityEvent ORDER BY id DESC")
    fun getPagingSource(): PagingSource<Int, EntityEvent>

    @Query("SELECT * FROM EntityEvent ORDER BY id DESC")
    fun pagingSource(): PagingSource<Int, EntityEvent>

    @Query("SELECT COUNT(*) == 0 from EntityEvent")
    suspend fun isEmpty(): Boolean

    @Query("SELECT * FROM EntityEvent ORDER BY id DESC LIMIT :count")
    fun getLatestPosts(count: Int = 1): Flow<List<EntityEvent>>

    @Query("UPDATE EntityEvent SET content = :content WHERE id=:id")
    suspend fun updateContentById(id: Long, content: String)

    suspend fun savePost(entityEvent: EntityEvent) =
        if (entityEvent.id == 0L) insert(entityEvent) else updateContentById(
            entityEvent.id,
            entityEvent.content
        )


    @Query(
        """
            UPDATE EntityEvent SET
            likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
            WHERE id=:id
        """
    )
    fun handleLike(id: Long)

    @Query("DELETE FROM EntityEvent WHERE id=:id")
    suspend fun removeById(id: Long)

    @Query("DELETE FROM EntityEvent")
    fun clear()

    @Insert
    suspend fun insert(entity: EntityEvent)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: List<EntityEvent>)
}
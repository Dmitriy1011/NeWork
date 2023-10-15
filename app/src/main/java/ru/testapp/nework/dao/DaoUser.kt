package ru.testapp.nework.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.testapp.nework.entity.EntityUser

@Dao
interface DaoUser {
    @Query("SELECT * FROM EntityUser")
    fun getAllUsers(): Flow<List<EntityUser>>

    @Insert
    suspend fun insert(entity: EntityUser)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: List<EntityUser>)
}
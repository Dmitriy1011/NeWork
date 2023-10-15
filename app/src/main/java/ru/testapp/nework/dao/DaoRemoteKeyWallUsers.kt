package ru.testapp.nework.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.testapp.nework.entity.EntityRemoteKeyWallUser

@Dao
interface DaoRemoteKeyWallUsers {
    @Query("SELECT max(`key`) FROM EntityRemoteKeyWallUser")
    fun max(): Long?

    @Query("SELECT min(`key`) FROM EntityRemoteKeyWallUser")
    fun min(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: EntityRemoteKeyWallUser)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: List<EntityRemoteKeyWallUser>)

    @Query("DELETE FROM EntityRemoteKeyWallUser")
    fun clear()

    @Query("SELECT COUNT(*) == 0 FROM EntityRemoteKeyWallUser")
    fun isEmpty(): Boolean
}
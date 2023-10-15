package ru.testapp.nework.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.testapp.nework.entity.EntityRemoteKeyWallMy

@Dao
interface DaoRemoteKeyWallMy {
    @Query("SELECT max(`key`) FROM EntityRemoteKeyWallMy")
    fun max(): Long?

    @Query("SELECT min(`key`) FROM EntityRemoteKeyWallMy")
    fun min(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: EntityRemoteKeyWallMy)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: List<EntityRemoteKeyWallMy>)

    @Query("DELETE FROM EntityRemoteKeyWallMy")
    fun clear()

    @Query("SELECT COUNT(*) == 0 FROM EntityRemoteKeyWallMy")
    fun isEmpty(): Boolean
}
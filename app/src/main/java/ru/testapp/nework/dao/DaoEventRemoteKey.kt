package ru.testapp.nework.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.testapp.nework.entity.EntityEventRemoteKey

@Dao
interface DaoEventRemoteKey {
    @Query("SELECT max(`key`) FROM EntityEventRemoteKey")
    fun max(): Long?

    @Query("SELECT min(`key`) FROM EntityEventRemoteKey")
    fun min(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: EntityEventRemoteKey)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: List<EntityEventRemoteKey>)

    @Query("DELETE FROM EntityEventRemoteKey")
    fun clear()

    @Query("SELECT COUNT(*) == 0 FROM EntityEventRemoteKey")
    fun isEmpty(): Boolean
}
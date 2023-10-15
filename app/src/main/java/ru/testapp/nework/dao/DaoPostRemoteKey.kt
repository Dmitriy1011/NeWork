package ru.testapp.nework.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.testapp.nework.entity.EntityPostRemoteKey

@Dao
interface DaoPostRemoteKey {
    @Query("SELECT max(`key`) FROM EntityPostRemoteKey")
    fun max(): Long?

    @Query("SELECT min(`key`) FROM EntityPostRemoteKey")
    fun min(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: EntityPostRemoteKey)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: List<EntityPostRemoteKey>)

    @Query("DELETE FROM EntityPostRemoteKey")
    fun clear()

    @Query("SELECT COUNT(*) == 0 FROM EntityPostRemoteKey")
    fun isEmpty(): Boolean
}

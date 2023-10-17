package ru.testapp.nework.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.testapp.nework.entity.EntityJob

@Dao
interface DaoJob {
    @Query("SELECT * FROM EntityJob ORDER BY id DESC")
    fun getMyJobs(): Flow<List<EntityJob>>

    @Query("SELECT * FROM EntityJob WHERE id = :id ORDER BY id DESC")
    fun getUsersJobs(id: Long): Flow<List<EntityJob>>

    @Query("UPDATE EntityJob SET name = :name, position = :position WHERE id = :id")
    suspend fun updateJob(id: Long, name: String, position: String)

    suspend fun saveJob(job: EntityJob) =
        if (job.id == 0L) insert(job) else updateJob(job.id, job.name, job.position)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entityJob: EntityJob)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entityJobList: List<EntityJob>)

    @Query("DELETE FROM EntityJob WHERE id = :id")
    suspend fun removeJob(id: Long)
}
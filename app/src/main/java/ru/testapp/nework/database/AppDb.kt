package ru.testapp.nework.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.testapp.nework.dao.DaoEvent
import ru.testapp.nework.dao.DaoEventRemoteKey
import ru.testapp.nework.dao.DaoPost
import ru.testapp.nework.dao.DaoPostRemoteKey
import ru.testapp.nework.dao.DaoUser
import ru.testapp.nework.entity.EntityEvent
import ru.testapp.nework.entity.EntityEventRemoteKey
import ru.testapp.nework.entity.EntityJob
import ru.testapp.nework.entity.EntityPostRemoteKey
import ru.testapp.nework.entity.EntityRemoteKeyWallMy
import ru.testapp.nework.entity.EntityRemoteKeyWallUser
import ru.testapp.nework.entity.EntityUser
import ru.testapp.nework.entity.PostEntity
import ru.testapp.nework.utils.TypeConverter

@Database(
    entities = [
        PostEntity::class,
        EntityEvent::class,
        EntityUser::class,
        EntityPostRemoteKey::class,
        EntityEventRemoteKey::class,
        EntityJob::class,
        EntityRemoteKeyWallMy::class,
        EntityRemoteKeyWallUser::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TypeConverter::class)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): DaoPost
    abstract fun eventDao(): DaoEvent
    abstract fun userDao(): DaoUser
    abstract fun postRemoteKeyDao(): DaoPostRemoteKey
    abstract fun eventRemoteKeyDao(): DaoEventRemoteKey
}
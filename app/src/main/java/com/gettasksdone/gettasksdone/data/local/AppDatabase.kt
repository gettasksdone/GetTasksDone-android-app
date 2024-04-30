package com.gettasksdone.gettasksdone.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gettasksdone.gettasksdone.data.local.dao.CheckItemDao
import com.gettasksdone.gettasksdone.data.local.dao.ContextDao
import com.gettasksdone.gettasksdone.data.local.dao.NoteDao
import com.gettasksdone.gettasksdone.data.local.dao.ProjectDao
import com.gettasksdone.gettasksdone.data.local.dao.TagDao
import com.gettasksdone.gettasksdone.data.local.dao.TaskDao
import com.gettasksdone.gettasksdone.data.local.dao.UserDao
import com.gettasksdone.gettasksdone.data.local.dao.UserInfoDao
import com.gettasksdone.gettasksdone.data.local.entities.CheckItemEntity
import com.gettasksdone.gettasksdone.data.local.entities.ContextEntity
import com.gettasksdone.gettasksdone.data.local.entities.NoteEntity
import com.gettasksdone.gettasksdone.data.local.entities.ProjectEntity
import com.gettasksdone.gettasksdone.data.local.entities.TagEntity
import com.gettasksdone.gettasksdone.data.local.entities.TagProjectCrossRef
import com.gettasksdone.gettasksdone.data.local.entities.TagTaskCrossRef
import com.gettasksdone.gettasksdone.data.local.entities.TaskEntity
import com.gettasksdone.gettasksdone.data.local.entities.UserEntity
import com.gettasksdone.gettasksdone.data.local.entities.UserInfoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [
    CheckItemEntity::class,
    ContextEntity::class,
    NoteEntity::class,
    ProjectEntity::class,
    TagEntity::class,
    TaskEntity::class,
    UserEntity::class,
    UserInfoEntity::class,
    TagProjectCrossRef::class,
    TagTaskCrossRef::class
                     ],
    version = 1,
    exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun checkItemDao(): CheckItemDao
    abstract fun contextDao(): ContextDao
    abstract fun noteDao(): NoteDao
    abstract fun projectDao(): ProjectDao
    abstract fun tagDao(): TagDao
    abstract fun taskDao(): TaskDao
    abstract fun userDao(): UserDao
    abstract fun userInfoDao(): UserInfoDao
    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "get-tasks-done"
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
    private class AppDatabaseCallback(private val scope: CoroutineScope): RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database -> scope.launch { populateDatabase(database.contextDao()) } }
        }
        suspend fun populateDatabase(contextDao: ContextDao){
            contextDao.deleteAll()
            var context = ContextEntity(1, "PruebaLocal")
            contextDao.upsert(context)
            context = ContextEntity(2, "PruebaLocal2")
            contextDao.upsert(context)
        }
    }
}
package com.gettasksdone.gettasksdone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.gettasksdone.gettasksdone.data.local.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getAll(): Flow<List<TaskEntity>>
    @Query("SELECT * FROM task WHERE id=(:taskId)")
    fun loadById(taskId: Long): Flow<List<TaskEntity>>
    @Insert
    suspend fun insertAll(vararg tasks: TaskEntity)
    @Upsert
    suspend fun upsert(task: TaskEntity)
    @Delete
    suspend fun delete(task: TaskEntity)
}
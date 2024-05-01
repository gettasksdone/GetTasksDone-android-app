package com.gettasksdone.gettasksdone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.gettasksdone.gettasksdone.data.local.entities.TaskEntity
import com.gettasksdone.gettasksdone.data.local.entities.TaskWithCheckItems
import com.gettasksdone.gettasksdone.data.local.entities.TaskWithNotes
import com.gettasksdone.gettasksdone.data.local.entities.TaskWithTags
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getAll(): List<TaskEntity>
    @Transaction
    @Query("SELECT * FROM task")
    fun getAllWithChecks(): Flow<List<TaskWithCheckItems>>
    @Transaction
    @Query("SELECT * FROM task")
    fun getAllWithTags(): Flow<List<TaskWithTags>>
    @Transaction
    @Query("SELECT * FROM task")
    fun getAllWithNotes(): Flow<List<TaskWithNotes>>
    @Query("SELECT * FROM task WHERE taskId=(:taskId)")
    fun loadById(taskId: Long): Flow<List<TaskEntity>>
    @Transaction
    @Query("SELECT * FROM task WHERE taskId=(:taskId)")
    fun loadByIdWithChecks(taskId: Long): Flow<List<TaskWithCheckItems>>
    @Transaction
    @Query("SELECT * FROM task WHERE taskId=(:taskId)")
    fun loadByIdWithTags(taskId: Long): Flow<List<TaskWithTags>>
    @Transaction
    @Query("SELECT * FROM task WHERE taskId=(:taskId)")
    fun loadByIdWithNotes(taskId: Long): Flow<List<TaskWithNotes>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg tasks: TaskEntity)
    @Upsert
    suspend fun upsert(task: TaskEntity)
    @Delete
    suspend fun delete(task: TaskEntity)
}
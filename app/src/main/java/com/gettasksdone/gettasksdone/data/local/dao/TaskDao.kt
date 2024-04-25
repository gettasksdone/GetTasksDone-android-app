package com.gettasksdone.gettasksdone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.gettasksdone.gettasksdone.data.local.entities.TaskEntity

@Dao
interface TaskDao {
    @Query("SELECT * FROM TaskEntity")
    fun getAll(): List<TaskEntity>
    @Query("SELECT * FROM TaskEntity WHERE id IN (:taskIds)")
    fun loadAllByIds(taskIds: LongArray): List<TaskEntity>
    @Insert
    fun insertAll(vararg tasks: TaskEntity)
    @Delete
    fun delete(task: TaskEntity)
}
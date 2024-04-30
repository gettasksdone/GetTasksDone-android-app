package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.layout.TaskEM
import com.gettasksdone.gettasksdone.data.local.dao.CheckItemDao
import com.gettasksdone.gettasksdone.data.local.dao.TaskDao
import com.gettasksdone.gettasksdone.data.local.entities.CheckItemEntity
import com.gettasksdone.gettasksdone.data.local.entities.TaskEntity
import com.gettasksdone.gettasksdone.model.Task
import kotlinx.coroutines.flow.Flow

open class TaskRepository(private val taskDao: TaskDao) {
    @WorkerThread
    fun getAll(): Flow<List<TaskEntity>>{
        return taskDao.getAll()
    }
    @WorkerThread
    fun get(task: Long): Flow<List<TaskEntity>> {
        return taskDao.loadById(task)
    }

    @WorkerThread
    suspend fun upsert(task: TaskEntity){
        taskDao.upsert(task)
    }
    @WorkerThread
    suspend fun delete(task: TaskEntity){
        taskDao.delete(task)
    }
}
package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.layout.ContextEM
import com.gettasksdone.gettasksdone.data.local.dao.ContextDao
import com.gettasksdone.gettasksdone.data.local.entities.ContextEntity
import com.gettasksdone.gettasksdone.data.local.entities.ContextWithTasks
import com.gettasksdone.gettasksdone.model.Context
import kotlinx.coroutines.flow.Flow

open class ContextRepository(private val contextDao: ContextDao) {
    @WorkerThread
    fun getAll(): Flow<List<ContextEntity>>{
        return contextDao.getAll()
    }
    @WorkerThread
    fun getAllWithTasks(): Flow<List<ContextWithTasks>>{
        return contextDao.getAllWithTasks()
    }
    @WorkerThread
    fun get(context: Long): Flow<List<ContextEntity>> {
        return contextDao.loadById(context)
    }
    @WorkerThread
    fun getWithTasks(context: Long): Flow<List<ContextWithTasks>> {
        return contextDao.loadByIdWithTasks(context)
    }
    @WorkerThread
    suspend fun upsert(context: ContextEntity){
        contextDao.upsert(context)
    }
    @WorkerThread
    suspend fun delete(context: ContextEntity){
        contextDao.delete(context)
    }
}
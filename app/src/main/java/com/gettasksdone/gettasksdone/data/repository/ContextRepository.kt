package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.local.dao.ContextDao
import com.gettasksdone.gettasksdone.data.local.entities.ContextEntity
import kotlinx.coroutines.flow.Flow

class ContextRepository(private val contextDao: ContextDao) {
    val allContexts: Flow<List<ContextEntity>> = contextDao.getAll()
    @WorkerThread
    fun get(context: Long){
        contextDao.loadById(context)
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
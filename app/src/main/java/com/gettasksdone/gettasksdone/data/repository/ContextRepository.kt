package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.layout.ContextEM
import com.gettasksdone.gettasksdone.data.local.dao.ContextDao
import com.gettasksdone.gettasksdone.data.local.entities.ContextEntity
import com.gettasksdone.gettasksdone.data.local.entities.ContextWithTasks
import com.gettasksdone.gettasksdone.model.Context
import kotlinx.coroutines.flow.Flow

class ContextRepository(private val contextDao: ContextDao) {
    val allContexts: Flow<List<ContextEntity>> = contextDao.getAll()
    val allContextsWithTasks: Flow<List<ContextWithTasks>> = contextDao.getAllWithTasks()
    @WorkerThread
    fun get(context: Long){
        contextDao.loadById(context)
    }
    @WorkerThread
    fun getWithTasks(context: Long){
        contextDao.loadByIdWithTasks(context)
    }
    @WorkerThread
    suspend fun upsert(context: ContextEntity){
        contextDao.upsert(context)
    }
    @WorkerThread
    suspend fun delete(context: ContextEntity){
        contextDao.delete(context)
    }
    @WorkerThread
    fun Context.asEntity() = ContextEntity(
        id = id,
        nombre = nombre
    )
    @WorkerThread
    fun ContextEntity.asExternalModel() = ContextEM(
        id = id,
        nombre = nombre
    )
}
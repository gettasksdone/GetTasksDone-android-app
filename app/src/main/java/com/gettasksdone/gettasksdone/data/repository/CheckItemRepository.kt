package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.layout.CheckItemEM
import com.gettasksdone.gettasksdone.data.local.dao.CheckItemDao
import com.gettasksdone.gettasksdone.data.local.entities.CheckItemEntity
import com.gettasksdone.gettasksdone.model.CheckItem
import kotlinx.coroutines.flow.Flow

class CheckItemRepository(private val checkItemDao: CheckItemDao) {
    val allCheckItems: Flow<List<CheckItemEntity>> = checkItemDao.getAll()
    @WorkerThread
    fun get(checkItem: Long){
        checkItemDao.loadById(checkItem)
    }
    @WorkerThread
    suspend fun upsert(checkItem: CheckItemEntity){
        checkItemDao.upsert(checkItem)
    }
    @WorkerThread
    suspend fun delete(checkItem: CheckItemEntity){
        checkItemDao.delete(checkItem)
    }
    @WorkerThread
    fun CheckItem.asEntity() = CheckItemEntity(
        id = id,
        contenido = contenido,
        estaMarcado = esta_marcado,
        taskId = tareaId
    )
    @WorkerThread
    fun CheckItemEntity.asExternalModel() = CheckItemEM(
        id = id,
        contenido = contenido,
        estaMarcado = estaMarcado
    )
}
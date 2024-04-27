package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.local.dao.CheckItemDao
import com.gettasksdone.gettasksdone.data.local.entities.CheckItemEntity
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
}
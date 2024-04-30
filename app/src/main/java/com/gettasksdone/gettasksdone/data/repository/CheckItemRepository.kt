package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.local.dao.CheckItemDao
import com.gettasksdone.gettasksdone.data.local.entities.CheckItemEntity
import com.gettasksdone.gettasksdone.model.CheckItem
import kotlinx.coroutines.flow.Flow

open class CheckItemRepository(private val checkItemDao: CheckItemDao) {
    @WorkerThread
    fun getAll(): Flow<List<CheckItemEntity>> {
        return checkItemDao.getAll()
    }
    @WorkerThread
    fun get(checkItem: Long): Flow<List<CheckItemEntity>>{
        return checkItemDao.loadById(checkItem)
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
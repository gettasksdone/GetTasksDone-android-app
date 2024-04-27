package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.local.dao.TagDao
import com.gettasksdone.gettasksdone.data.local.entities.TagEntity
import kotlinx.coroutines.flow.Flow

class TagRepository(private val tagDao: TagDao) {
    val allCheckItems: Flow<List<TagEntity>> = tagDao.getAll()
    @WorkerThread
    fun get(tag: Long){
        tagDao.loadById(tag)
    }

    @WorkerThread
    suspend fun upsert(tag: TagEntity){
        tagDao.upsert(tag)
    }
    @WorkerThread
    suspend fun delete(tag: TagEntity){
        tagDao.delete(tag)
    }
}
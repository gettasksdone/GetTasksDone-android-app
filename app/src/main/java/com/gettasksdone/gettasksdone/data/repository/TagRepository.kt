package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.layout.TagEM
import com.gettasksdone.gettasksdone.data.local.dao.TagDao
import com.gettasksdone.gettasksdone.data.local.entities.TagEntity
import com.gettasksdone.gettasksdone.model.Tag
import kotlinx.coroutines.flow.Flow

open class TagRepository(private val tagDao: TagDao) {
    @WorkerThread
    fun getAll(): Flow<List<TagEntity>>{
        return tagDao.getAll()
    }
    @WorkerThread
    fun get(tag: Long): Flow<List<TagEntity>> {
        return tagDao.loadById(tag)
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
package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.data.layout.TagEM
import com.gettasksdone.gettasksdone.data.local.dao.TagDao
import com.gettasksdone.gettasksdone.data.local.entities.TagEntity
import com.gettasksdone.gettasksdone.data.mapper.toDomain
import com.gettasksdone.gettasksdone.data.mapper.toEntity
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.model.Context
import com.gettasksdone.gettasksdone.model.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

open class TagRepository(
    private val api: ApiService,
    private val jwtHelper: JwtHelper,
    private val tagDao: TagDao) {

    suspend fun getAll(): List<Tag>{
        return withContext(Dispatchers.IO){
            var localTags = tagDao.getAll()
            if(localTags.isEmpty()){
                val remoteTags = getTagsRemote()
                remoteTags.forEach {
                    tagDao.insertAll(it.toEntity())
                }
                localTags = tagDao.getAll()
            }
            localTags.map { it.toDomain() }
        }
    }
    private suspend fun getTagsRemote(): List<Tag>{
        return withContext(Dispatchers.IO){
            try{
                val authHeader = "Bearer ${jwtHelper.getToken()}"
                val call = api.getTags(authHeader)
                val response = call.execute()
                if(response.isSuccessful){
                    response.body() ?: emptyList()
                }else{
                    emptyList()
                }
            }catch (e: Exception){
                emptyList()
            }
        }
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
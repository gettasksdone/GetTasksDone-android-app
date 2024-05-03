package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.data.layout.ContextEM
import com.gettasksdone.gettasksdone.data.local.dao.ContextDao
import com.gettasksdone.gettasksdone.data.local.entities.ContextEntity
import com.gettasksdone.gettasksdone.data.local.entities.ContextWithTasks
import com.gettasksdone.gettasksdone.data.mapper.toDomain
import com.gettasksdone.gettasksdone.data.mapper.toEntity
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.model.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

open class ContextRepository(
    private val api: ApiService,
    private val jwtHelper: JwtHelper,
    private val contextDao: ContextDao
) {
    suspend fun getAll(): List<Context>{
        return withContext(Dispatchers.IO){
            var localContexts = contextDao.getAll()
            if(localContexts.isEmpty()){
                val remoteContexts = getContextsRemote()
                remoteContexts.forEach{
                    contextDao.insertAll(it.toEntity())
                }
                localContexts = contextDao.getAll()
            }
            localContexts.map { it.toDomain() }
        }
    }
    private suspend fun getContextsRemote(): List<Context>{
        return withContext(Dispatchers.IO){
            try{
                val authHeader = "Bearer ${jwtHelper.getToken()}"
                val call = api.getContexts(authHeader)
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
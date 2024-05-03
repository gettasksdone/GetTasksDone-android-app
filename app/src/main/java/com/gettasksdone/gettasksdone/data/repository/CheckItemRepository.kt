package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.data.local.dao.CheckItemDao
import com.gettasksdone.gettasksdone.data.local.entities.CheckItemEntity
import com.gettasksdone.gettasksdone.data.mapper.toDomain
import com.gettasksdone.gettasksdone.data.mapper.toEntity
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.model.CheckItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

open class CheckItemRepository(
    private val api: ApiService,
    private val jwtHelper: JwtHelper,
    private val checkItemDao: CheckItemDao) {
    suspend fun getAll(): List<CheckItem> {
        return withContext(Dispatchers.IO){
            var localChecks = checkItemDao.getAll()
            if(localChecks.isEmpty()){
                val remoteChecks = getChecksRemote()
                remoteChecks.forEach{
                    checkItemDao.insertAll(it.toEntity())
                }
                localChecks = checkItemDao.getAll()
            }
            localChecks.map { it.toDomain() }
        }
    }

    private suspend fun getChecksRemote(): List<CheckItem>{
        return withContext(Dispatchers.IO){
            try{
                val authHeader = "Bearer ${jwtHelper.getToken()}"
                val call = api.getChecks(authHeader)
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
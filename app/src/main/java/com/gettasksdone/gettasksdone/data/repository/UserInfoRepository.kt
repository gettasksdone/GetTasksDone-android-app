package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.data.layout.UserInfoEM
import com.gettasksdone.gettasksdone.data.local.dao.UserInfoDao
import com.gettasksdone.gettasksdone.data.local.entities.UserInfoEntity
import com.gettasksdone.gettasksdone.data.mapper.toDomain
import com.gettasksdone.gettasksdone.data.mapper.toEntity
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.model.Context
import com.gettasksdone.gettasksdone.model.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

open class UserInfoRepository(
    private val api: ApiService,
    private val jwtHelper: JwtHelper,
    private val userInfoDao: UserInfoDao) {

    suspend fun getAll(): List<UserInfo>{
        return withContext(Dispatchers.IO){
            var localUserData = userInfoDao.getAll()
            if(localUserData.isEmpty()){
                val remoteUserData = getUserInfoRemote()
                if (remoteUserData != null) {
                    userInfoDao.insertAll(remoteUserData.toEntity())
                }
                localUserData = userInfoDao.getAll()
            }
            localUserData.map { it.toDomain() }
        }
    }
    private suspend fun getUserInfoRemote(): UserInfo?{
        return withContext(Dispatchers.IO){
            try{
                val authHeader = "Bearer ${jwtHelper.getToken()}"
                val call = api.getUserData(authHeader)
                val response = call.execute()
                if(response.isSuccessful){
                    response.body()
                }else{
                    null
                }
            }catch (e: Exception){
                null
            }
        }
    }
    @WorkerThread
    fun get(userData: Long): Flow<List<UserInfoEntity>> {
        return userInfoDao.loadById(userData)
    }

    @WorkerThread
    suspend fun upsert(userData: UserInfoEntity){
        userInfoDao.upsert(userData)
    }
    @WorkerThread
    suspend fun delete(userData: UserInfoEntity){
        userInfoDao.delete(userData)
    }
}
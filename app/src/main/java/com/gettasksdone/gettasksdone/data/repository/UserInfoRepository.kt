package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.layout.UserInfoEM
import com.gettasksdone.gettasksdone.data.local.dao.UserInfoDao
import com.gettasksdone.gettasksdone.data.local.entities.UserInfoEntity
import com.gettasksdone.gettasksdone.model.UserInfo
import kotlinx.coroutines.flow.Flow

open class UserInfoRepository(private val userInfoDao: UserInfoDao) {
    @WorkerThread
    fun getAll(): Flow<List<UserInfoEntity>>{
        return userInfoDao.getAll()
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
package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.local.dao.UserInfoDao
import com.gettasksdone.gettasksdone.data.local.entities.UserInfoEntity
import kotlinx.coroutines.flow.Flow

class UserInfoRepository(private val userInfoDao: UserInfoDao) {
    val allCheckItems: Flow<List<UserInfoEntity>> = userInfoDao.getAll()
    @WorkerThread
    fun get(userData: Long){
        userInfoDao.loadById(userData)
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
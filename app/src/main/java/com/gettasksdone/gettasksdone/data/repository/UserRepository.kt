package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.layout.UserEM
import com.gettasksdone.gettasksdone.data.local.dao.UserDao
import com.gettasksdone.gettasksdone.data.local.entities.UserAndUserInfo
import com.gettasksdone.gettasksdone.data.local.entities.UserEntity
import com.gettasksdone.gettasksdone.model.Rol
import com.gettasksdone.gettasksdone.model.User
import kotlinx.coroutines.flow.Flow

open class UserRepository(private val userDao: UserDao) {
    @WorkerThread
    fun getAll(): Flow<List<UserEntity>>{
        return userDao.getAll()
    }
    @WorkerThread
    fun getAllWithData(): Flow<List<UserAndUserInfo>>{
        return userDao.getAllWithData()
    }
    @WorkerThread
    fun get(user: Long): Flow<List<UserEntity>> {
        return userDao.loadById(user)
    }
    @WorkerThread
    fun getWithData(user: Long): Flow<List<UserAndUserInfo>> {
        return userDao.loadByIdWithData(user)
    }
    @WorkerThread
    suspend fun upsert(user: UserEntity){
        userDao.upsert(user)
    }
    @WorkerThread
    suspend fun delete(user: UserEntity){
        userDao.delete(user)
    }
}
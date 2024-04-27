package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.local.dao.UserDao
import com.gettasksdone.gettasksdone.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val allCheckItems: Flow<List<UserEntity>> = userDao.getAll()
    @WorkerThread
    fun get(user: Long){
        userDao.loadById(user)
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
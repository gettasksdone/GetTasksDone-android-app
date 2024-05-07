package com.gettasksdone.gettasksdone.data.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.data.layout.UserEM
import com.gettasksdone.gettasksdone.data.local.dao.UserDao
import com.gettasksdone.gettasksdone.data.local.entities.UserAndUserInfo
import com.gettasksdone.gettasksdone.data.local.entities.UserEntity
import com.gettasksdone.gettasksdone.data.mapper.toDomain
import com.gettasksdone.gettasksdone.data.mapper.toEntity
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.model.Rol
import com.gettasksdone.gettasksdone.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

open class UserRepository(
    private val api: ApiService?,
    private val jwtHelper: JwtHelper,
    private val userDao: UserDao) {

    suspend fun getAll(): List<User>{
        return withContext(Dispatchers.IO){
            try{
                val authHeader = "Bearer ${jwtHelper.getToken()}"
                val call = api?.getAuthedUser(authHeader)
                val response = call?.execute()
                if (response != null) {
                    if(response.isSuccessful){
                        userDao.deleteAll()
                        val remoteUser = response.body()
                        userDao.insertAll(remoteUser?.toEntity()!!)
                    }
                }
            }catch (e: Exception){
                Log.w("UserRepository", "No hay conexión con la red")
            }
            val localUsers: List<UserEntity> = userDao.getAll()
            localUsers.map { it.toDomain() }
        }
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
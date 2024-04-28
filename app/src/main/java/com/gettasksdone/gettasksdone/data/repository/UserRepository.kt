package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.layout.UserEM
import com.gettasksdone.gettasksdone.data.local.dao.UserDao
import com.gettasksdone.gettasksdone.data.local.entities.UserAndUserInfo
import com.gettasksdone.gettasksdone.data.local.entities.UserEntity
import com.gettasksdone.gettasksdone.model.Rol
import com.gettasksdone.gettasksdone.model.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val allCheckItems: Flow<List<UserEntity>> = userDao.getAll()
    val allUsersWithData: Flow<List<UserAndUserInfo>> = userDao.getAllWithData()
    @WorkerThread
    fun get(user: Long){
        userDao.loadById(user)
    }
    @WorkerThread
    fun getWithData(user: Long){
        userDao.loadByIdWithData(user)
    }
    @WorkerThread
    suspend fun upsert(user: UserEntity){
        userDao.upsert(user)
    }
    @WorkerThread
    suspend fun delete(user: UserEntity){
        userDao.delete(user)
    }
    @WorkerThread
    fun User.asEntity() = UserEntity(
        id = id,
        username = username,
        email = email,
        rol = rol
    )
    @WorkerThread
    fun UserEntity.asExternalModel() = UserEM(
        id = id,
        username = username,
        email = email,
        rol = when(rol){
            Rol.USUARIO -> "Usuario"
            Rol.ADMINISTRADOR -> "Administrador"
        }
    )
}
package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.layout.UserInfoEM
import com.gettasksdone.gettasksdone.data.local.dao.UserInfoDao
import com.gettasksdone.gettasksdone.data.local.entities.UserInfoEntity
import com.gettasksdone.gettasksdone.model.UserInfo
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
    @WorkerThread
    fun UserInfo.asEntity() = UserInfoEntity(
        id = id,
        departamento = departamento,
        nombre = nombre,
        puesto = puesto,
        telefono = telefono,
        userId = usuarioId
    )
    @WorkerThread
    fun UserInfoEntity.asExternalModel() = UserInfoEM(
        id = id,
        nombre = nombre,
        puesto = puesto,
        departamento = departamento,
        telefono = telefono
    )
}
package com.gettasksdone.gettasksdone.model

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.local.entities.UserEntity


/*
    {
        "id": 12345
        "username": "victor",
        "email": "prueba@prueba.com",
        "rol": 0
    }
*/

enum class Rol {
    USUARIO, ADMINISTRADOR
}
data class User(
    val id: Long,
    val username: String,
    val email: String,
    val rol: Rol
){
    fun asEntity(): UserEntity = UserEntity(
        id = id,
        username = username,
        email = email,
        rol = rol
    )
}

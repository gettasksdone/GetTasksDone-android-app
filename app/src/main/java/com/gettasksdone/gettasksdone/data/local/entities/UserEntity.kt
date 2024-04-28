package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gettasksdone.gettasksdone.model.Rol


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
@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "rol") val rol: Rol
)

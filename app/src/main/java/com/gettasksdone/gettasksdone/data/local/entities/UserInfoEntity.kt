package com.gettasksdone.gettasksdone.data.local.entities

import androidx.annotation.WorkerThread
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gettasksdone.gettasksdone.data.layout.UserInfoEM

/*
    {
        "id": 1,
        "nombre": "Nombre de prueba",
        "telefono": 123456789,
        "puesto": "Gerente",
        "departamento": "Ciberseguridad"
    }
*/
@Entity(tableName = "userInfo")
data class UserInfoEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "puesto") val puesto: String,
    @ColumnInfo(name = "departamento") val departamento: String,
    @ColumnInfo(name = "userId") val userId: Long,
    @ColumnInfo(name = "telefono") val telefono: Long? = null
){
    fun asExternalModel(): UserInfoEM = UserInfoEM(
        id = id,
        nombre = nombre,
        puesto = puesto,
        departamento = departamento,
        telefono = telefono
    )
}
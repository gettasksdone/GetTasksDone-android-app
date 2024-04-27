package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    @ColumnInfo(name = "telefono") val telefono: Long?,
    @ColumnInfo(name = "puesto") val puesto: String,
    @ColumnInfo(name = "departamento") val departamento: String
)
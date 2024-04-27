package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
    {
        "id": 1234,
        "nombre": "Base de Datos"
    }
*/
@Entity(tableName = "context")
data class ContextEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "nombre") val nombre: String
)
package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
    {
        "id": 12345
        "nombre": "Mejora"
    }
*/
@Entity
data class TagEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "nombre") val nombre: String
    //TODO - Investigar la definicion de relaciones entre objetos
)
package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
    {
        "id": 1,
        "contenido": "Check item de prueba",
        "esta_marcado": false
    }
*/
@Entity(tableName = "checkItem")
data class CheckItemEntity (
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "contenido") val contenido: String,
    @ColumnInfo(name = "esta_marcado") val esta_marcado: Boolean
    //TODO - Investigar la definicion de relaciones entre objetos
)
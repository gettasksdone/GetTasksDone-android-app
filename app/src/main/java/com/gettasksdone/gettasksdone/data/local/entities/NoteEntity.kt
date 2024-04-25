package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
    {
        "id": 4,
        "contenido": "Nota de prueba asignada a una tarea",
        "creacion": "2023-11-20T15:01:00"
    }
*/
@Entity
data class NoteEntity (
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "contenido") val contenido: String,
    @ColumnInfo(name = "creacion") val creacion: String
    //TODO - Investigar la definicion de relaciones entre objetos
)
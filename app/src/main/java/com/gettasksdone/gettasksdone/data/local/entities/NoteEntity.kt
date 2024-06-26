package com.gettasksdone.gettasksdone.data.local.entities

import androidx.annotation.WorkerThread
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gettasksdone.gettasksdone.data.layout.NoteEM

/*
    {
        "id": 4,
        "contenido": "Nota de prueba asignada a una tarea",
        "creacion": "2023-11-20T15:01:00"
    }
*/
@Entity(tableName = "note")
data class NoteEntity (
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "contenido") val contenido: String,
    @ColumnInfo(name = "creacion") val creacion: String,
    @ColumnInfo(name = "taskId") val taskId: Long? = null,
    @ColumnInfo(name = "projectId") val projectId: Long? = null
){
    fun asExternalModel(): NoteEM = NoteEM(
        id = id,
        contenido = contenido,
        creacion = creacion
    )
}
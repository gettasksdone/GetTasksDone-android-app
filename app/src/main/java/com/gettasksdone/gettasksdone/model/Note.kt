package com.gettasksdone.gettasksdone.model

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.local.entities.NoteEntity

/*
    {
        "id": 4,
        "contenido": "Nota de prueba asignada a una tarea",
        "creacion": "2023-11-20T15:01:00"
    }
*/
data class Note (
    val id: Long,
    val contenido: String,
    val creacion: String,
    val tareaId: Long? = null,
    val proyectoId: Long? = null
){
    fun asEntity(): NoteEntity = NoteEntity(
        id = id,
        contenido = contenido,
        creacion = creacion,
        projectId = proyectoId,
        taskId = tareaId
    )
}
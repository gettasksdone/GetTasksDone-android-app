package com.gettasksdone.gettasksdone.data.mapper

import com.gettasksdone.gettasksdone.data.local.entities.NoteEntity
import com.gettasksdone.gettasksdone.model.Note

fun Note.toEntity(): NoteEntity{
    return NoteEntity(
        id = this.id,
        contenido = this.contenido,
        creacion = this.creacion,
        taskId = this.tareaId,
        projectId = this.proyectoId
    )
}

fun NoteEntity.toDomain(): Note{
    return Note(
        id = this.id,
        contenido = this.contenido,
        creacion = this.creacion,
        tareaId = this.taskId,
        proyectoId = this.projectId
    )
}
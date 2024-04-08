package com.gettasksdone.gettasksdone.model

import java.time.LocalDateTime

data class Project (
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val estado: String,
    val inicio: LocalDateTime?,
    val fin: LocalDateTime,
    val tareas: List<Task> = emptyList(),
    val notas: List<Note> = emptyList(),
    val etiquetas: List<Tag> = emptyList()
)
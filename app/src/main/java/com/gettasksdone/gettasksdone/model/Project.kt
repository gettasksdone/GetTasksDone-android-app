package com.gettasksdone.gettasksdone.model

data class Project (
    val id: Long,
    val nombre: String,
    val estado: String,
    val descripcion: String? = null,
    val inicio: String? = null,
    val fin: String? = null,
    val tareas: List<Task> = emptyList(),
    val notas: List<Note> = emptyList(),
    val etiquetas: List<Tag> = emptyList()
)
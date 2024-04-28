package com.gettasksdone.gettasksdone.model

data class Task (
    val id: Long,
    val titulo: String,
    val estado: String,
    val prioridad: Int,
    val contexto: Context,
    val descripcion: String? = null,
    val creacion: String? = null,
    val vencimiento: String? = null,
    val proyectoId: Long,
    val contextoId: Long,
    val checkItems: List<CheckItem> = emptyList(),
    val notas: List<Note> = emptyList(),
    val etiquetas: List<Tag> = emptyList()
)
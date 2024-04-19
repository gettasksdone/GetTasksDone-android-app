package com.gettasksdone.gettasksdone.model

import java.time.LocalDateTime

data class Task (
    val id: Long,
    val titulo: String,
    val descripcion: String,
    val estado: String,
    val prioridad: Int,
    val contexto: Context,
    val creacion: String,
    val vencimiento: String,
    val checkItems: List<CheckItem> = emptyList(),
    val notas: List<Note> = emptyList(),
    val etiquetas: List<Tag> = emptyList()
)
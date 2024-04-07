package com.gettasksdone.gettasksdone.model

import java.time.LocalDateTime

data class Task (
    val id: Long,
    val descripcion: String,
    val status: String,
    val priority: Int,
    val contexto: Context,
    val creation: LocalDateTime?,
    val expiring: LocalDateTime?,
    val checkItems: List<CheckItem> = emptyList(),
    val notes: List<Note> = emptyList(),
    val tags: List<Tag> = emptyList()
)
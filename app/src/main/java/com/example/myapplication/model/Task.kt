package com.example.myapplication.model

import java.time.LocalDateTime

data class Task (
    val id: Long,
    val description: String,
    val status: String,
    val priority: Int,
    val context: Context,
    val creation: LocalDateTime?,
    val expiring: LocalDateTime?,
    val checkItems: List<CheckItem> = emptyList(),
    val notes: List<Note> = emptyList(),
    val tags: List<Tag> = emptyList()
)
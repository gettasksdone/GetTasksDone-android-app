package com.example.myapplication.model

import java.time.LocalDateTime

data class Project (
    val id: Long,
    val name: String,
    val description: String,
    val status: String,
    val startDate: LocalDateTime?,
    val finishDate: LocalDateTime,
    val tasks: List<Task> = emptyList(),
    val notes: List<Note> = emptyList(),
    val tags: List<Tag> = emptyList()
)
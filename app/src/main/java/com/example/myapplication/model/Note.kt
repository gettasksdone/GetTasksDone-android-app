package com.example.myapplication.model

import java.time.LocalDateTime

/*
    {
        "id": 4,
        "contenido": "Nota de prueba asignada a una tarea",
        "creacion": "2023-11-20T15:01:00"
    }
*/
data class Note (
    val id: Long,
    val content: String,
    val creation: LocalDateTime
)
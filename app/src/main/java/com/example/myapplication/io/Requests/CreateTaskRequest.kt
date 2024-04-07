package com.example.myapplication.io.Requests

data class CreateTaskRequest(
    val descripcion: String,
    val creacion: String,
    val vencimiento: String,
    val estado: String,
    val prioridad: Int
)

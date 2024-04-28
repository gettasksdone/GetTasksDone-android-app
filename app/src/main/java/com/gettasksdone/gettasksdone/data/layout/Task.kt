package com.gettasksdone.gettasksdone.data.layout

data class Task(
    val id: Long,
    val titulo: String,
    val descripcion: String? = null,
    val estado: String,
    val prioridad: Int,
    val creacion: String? = null,
    val vencimiento: String? = null
)

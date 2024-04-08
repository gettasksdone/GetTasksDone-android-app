package com.gettasksdone.gettasksdone.io.requests

data class ProjectRequest (
    val nombre: String,
    val inicio: String = getCurrentDate(),
    val fin: String,
    val descripcion: String,
    val estado: String
)
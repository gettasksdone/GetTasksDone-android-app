package com.gettasksdone.gettasksdone.io.requests

data class NoteRequest (
    val contenido: String,
    val creacion: String = getCurrentDate(),
)
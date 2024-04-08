package com.gettasksdone.gettasksdone.io.requests

data class CheckItemRequest (
    val contenido: String,
    val esta_marcado: Boolean
)
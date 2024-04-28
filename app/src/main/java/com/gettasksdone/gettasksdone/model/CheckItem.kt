package com.gettasksdone.gettasksdone.model

/*
    {
        "id": 1,
        "contenido": "Check item de prueba",
        "esta_marcado": false
    }
*/
data class CheckItem (
    val id: Long,
    val contenido: String,
    val esta_marcado: Boolean,
    val tareaId: Long
)
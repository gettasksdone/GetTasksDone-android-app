package com.example.myapplication.model

/*
    {
        "id": 1,
        "contenido": "Check item de prueba",
        "esta_marcado": false
    }
*/
data class CheckItem (
    val id: Long,
    val content: String,
    val isMarked: Boolean
)
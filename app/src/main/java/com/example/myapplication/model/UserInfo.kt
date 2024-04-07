package com.example.myapplication.model

/*
    {
        "id": 1,
        "nombre": "Nombre de prueba",
        "telefono": 123456789,
        "puesto": "Gerente",
        "departamento": "Ciberseguridad"
    }
*/
data class UserInfo(
    val id: Long,
    val name: String,
    val phone: Long,
    val role: String,
    val team: String
)
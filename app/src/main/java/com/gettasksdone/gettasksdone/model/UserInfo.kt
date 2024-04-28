package com.gettasksdone.gettasksdone.model

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
    val nombre: String,
    val telefono: Long? = null,
    val puesto: String,
    val departamento: String,
    val usuarioId: Long
)
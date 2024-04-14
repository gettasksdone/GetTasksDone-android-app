package com.gettasksdone.gettasksdone.model


/*
    {
        "id": 12345
        "username": "victor",
        "email": "prueba@prueba.com",
        "rol": 0
    }
*/

enum class Rol {
    USUARIO, ADMINISTRADOR
}
data class User(
    val id: Long,
    val username: String,
    val email: String,
    val rol: Rol
)

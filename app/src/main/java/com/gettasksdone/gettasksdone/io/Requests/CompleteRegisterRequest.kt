package com.gettasksdone.gettasksdone.io.Requests


data class UsuarioId(
    val id: Int
)

data class CompleteRegisterRequest(
    val usuario: UsuarioId,
    val nombre: String,
    val telefono: String,
    val puesto: String,
    val departamento: String
)

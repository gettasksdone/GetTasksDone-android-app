package com.gettasksdone.gettasksdone.data.layout

data class UserInfo(
    val id: Long,
    val nombre: String,
    val puesto: String,
    val departamento: String,
    val telefono: Long? = null
)

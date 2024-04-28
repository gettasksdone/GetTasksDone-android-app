package com.gettasksdone.gettasksdone.data.layout

data class Project(
    val id: Long,
    val nombre: String,
    val descripcion: String? = null,
    val estado: String,
    val inicio: String? = null,
    val fin: String? = null
)

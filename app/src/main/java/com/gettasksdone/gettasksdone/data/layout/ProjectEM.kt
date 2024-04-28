package com.gettasksdone.gettasksdone.data.layout

data class ProjectEM(
    val id: Long,
    val nombre: String,
    val descripcion: String? = null,
    val estado: String,
    val inicio: String? = null,
    val fin: String? = null
)

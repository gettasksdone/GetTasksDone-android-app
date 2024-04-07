package com.gettasksdone.gettasksdone.io.Responses

import com.gettasksdone.gettasksdone.io.Requests.Contexto

data class GetTasksResponse(
    val descripcion: String,
    val creacion: String,
    val vencimiento: String,
    val estado: String,
    val prioridad: Int,
    val contexto: Contexto,
    val checkItems: List<String>,
    val notas: List<String>,
    val etiquetas: List<String>
)

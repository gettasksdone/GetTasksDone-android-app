package com.gettasksdone.gettasksdone.model

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.local.entities.TaskEntity

data class Task (
    val id: Long,
    val titulo: String,
    val estado: String,
    val prioridad: Int,
    val contexto: Context,
    val descripcion: String? = null,
    val creacion: String? = null,
    val vencimiento: String? = null,
    val proyectoId: Long,
    val contextoId: Long,
    val checkItems: List<CheckItem> = emptyList(),
    val notas: List<Note> = emptyList(),
    val etiquetas: List<Tag> = emptyList()
){
    fun asEntity(): TaskEntity = TaskEntity(
        taskId = id,
        titulo = titulo,
        descripcion = descripcion,
        estado = estado,
        creacion = creacion,
        vencimiento = vencimiento,
        prioridad = prioridad,
        contexto = contextoId,
        proyecto = proyectoId
    )
}
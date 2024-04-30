package com.gettasksdone.gettasksdone.model

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.local.entities.ProjectEntity

data class Project (
    val id: Long,
    val nombre: String,
    val estado: String,
    val descripcion: String? = null,
    val inicio: String? = null,
    val fin: String? = null,
    val tareas: List<Task> = emptyList(),
    val notas: List<Note> = emptyList(),
    val etiquetas: List<Tag> = emptyList()
){
    fun asEntity(): ProjectEntity = ProjectEntity(
        projectId = id,
        nombre = nombre,
        descripcion = descripcion,
        estado = estado,
        inicio = inicio,
        fin = fin
    )
}
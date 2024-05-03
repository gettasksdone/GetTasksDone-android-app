package com.gettasksdone.gettasksdone.data.mapper

import com.gettasksdone.gettasksdone.data.local.entities.TaskEntity
import com.gettasksdone.gettasksdone.model.Context
import com.gettasksdone.gettasksdone.model.Task

fun Task.toEntity(): TaskEntity{
    return TaskEntity(
        taskId = this.id,
        contexto = this.contextoId,
        estado = this.estado,
        prioridad = this.prioridad,
        proyecto = this.proyectoId,
        titulo = this.titulo
    )
}

fun TaskEntity.toDomain(): Task {
    return Task(
        id = this.taskId,
        contextoId = this.contexto,
        estado = this.estado,
        prioridad = this.prioridad,
        proyectoId = this.proyecto,
        titulo = this.titulo,
        contexto = Context(1,"prueba offline")
    )
}


package com.gettasksdone.gettasksdone.data.mapper

import com.gettasksdone.gettasksdone.data.local.entities.ProjectEntity
import com.gettasksdone.gettasksdone.model.Project

fun Project.toEntity(): ProjectEntity{
    return ProjectEntity(
        projectId = this.id,
        nombre = this.nombre,
        descripcion = this.descripcion,
        estado = this.estado,
        inicio = this.inicio,
        fin = this.fin
    )
}

fun ProjectEntity.toDomain(): Project{
    return Project(
        id = this.projectId,
        nombre = this.nombre,
        estado = this.estado,
        descripcion = this.descripcion,
        inicio = this.inicio,
        fin = this.fin
    )
}
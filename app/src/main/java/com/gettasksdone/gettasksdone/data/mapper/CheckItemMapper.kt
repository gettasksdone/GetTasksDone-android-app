package com.gettasksdone.gettasksdone.data.mapper

import com.gettasksdone.gettasksdone.data.local.entities.CheckItemEntity
import com.gettasksdone.gettasksdone.model.CheckItem

fun CheckItem.toEntity(): CheckItemEntity{
    return CheckItemEntity(
        id = this.id,
        contenido = this.contenido,
        estaMarcado = esta_marcado,
        taskId = this.tareaId
    )
}

fun CheckItemEntity.toDomain(): CheckItem{
    return CheckItem(
        id = this.id,
        contenido = this.contenido,
        esta_marcado = this.estaMarcado,
        tareaId = this.taskId
    )
}
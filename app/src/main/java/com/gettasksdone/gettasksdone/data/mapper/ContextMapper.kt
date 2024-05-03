package com.gettasksdone.gettasksdone.data.mapper

import com.gettasksdone.gettasksdone.data.local.entities.ContextEntity
import com.gettasksdone.gettasksdone.model.Context

fun Context.toEntity(): ContextEntity{
    return ContextEntity(
        id = this.id,
        nombre = this.nombre
    )
}

fun ContextEntity.toDomain(): Context{
    return Context(
        id = this.id,
        nombre = this.nombre
    )
}
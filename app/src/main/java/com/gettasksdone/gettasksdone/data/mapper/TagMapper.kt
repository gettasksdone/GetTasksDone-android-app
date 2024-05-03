package com.gettasksdone.gettasksdone.data.mapper

import com.gettasksdone.gettasksdone.data.local.entities.TagEntity
import com.gettasksdone.gettasksdone.model.Tag

fun Tag.toEntity(): TagEntity{
    return TagEntity(
        tagId = this.id,
        nombre = this.nombre
    )
}

fun TagEntity.toDomain(): Tag{
    return Tag(
        id = this.tagId,
        nombre = this.nombre
    )
}
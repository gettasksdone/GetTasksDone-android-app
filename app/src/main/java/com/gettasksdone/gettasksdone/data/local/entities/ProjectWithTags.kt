package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ProjectWithTags(
    @Embedded val project: ProjectEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "etiquetas"
    )
    val tags: List<TagEntity>
)

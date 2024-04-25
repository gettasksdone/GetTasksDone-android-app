package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ContextWithTasks (
    @Embedded val context: ContextEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "contexto"
    )
    val tasks: List<TaskEntity>
)
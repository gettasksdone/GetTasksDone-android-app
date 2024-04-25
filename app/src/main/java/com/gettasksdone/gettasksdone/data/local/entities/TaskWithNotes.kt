package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithNotes(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "notas"
    )
    val notes: List<NoteEntity>
)

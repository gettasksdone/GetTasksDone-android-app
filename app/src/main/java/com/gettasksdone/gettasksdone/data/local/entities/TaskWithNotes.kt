package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithNotes(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "taskId",
        entityColumn = "taskId"
    )
    val notes: List<NoteEntity>?
)

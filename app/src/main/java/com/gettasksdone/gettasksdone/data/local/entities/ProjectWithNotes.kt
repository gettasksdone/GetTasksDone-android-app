package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ProjectWithNotes(
    @Embedded val project: ProjectEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "notas"
    )
    val notes: List<NoteEntity>
)
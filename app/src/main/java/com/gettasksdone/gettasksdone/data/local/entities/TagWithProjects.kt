package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TagWithProjects(
    @Embedded val project: TagEntity,
    @Relation(
        parentColumn = "tagId",
        entityColumn = "projectId",
        associateBy = Junction(TagProjectCrossRef::class)
    )
    val tags: List<ProjectEntity>
)

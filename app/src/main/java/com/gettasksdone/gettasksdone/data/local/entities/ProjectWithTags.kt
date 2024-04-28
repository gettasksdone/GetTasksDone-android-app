package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ProjectWithTags(
    @Embedded val project: ProjectEntity,
    @Relation(
        parentColumn = "projectId",
        entityColumn = "tagId",
        associateBy = Junction(TagProjectCrossRef::class)
    )
    val tags: List<TagEntity>
)

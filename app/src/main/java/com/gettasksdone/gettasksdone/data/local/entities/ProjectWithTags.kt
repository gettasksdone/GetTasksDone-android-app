package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ProjectWithTags(
    @Embedded val project: ProjectEntity,
    @Relation(
        associateBy = Junction(TagProjectCrossRef::class),
        parentColumn = "projectId",
        entityColumn = "tagId"
    )
    val tags: List<TagEntity>
)

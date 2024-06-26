package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TaskWithTags(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "taskId",
        entityColumn = "tagId",
        associateBy = Junction(TagTaskCrossRef::class)
    )
    val tags: List<TagEntity>
)

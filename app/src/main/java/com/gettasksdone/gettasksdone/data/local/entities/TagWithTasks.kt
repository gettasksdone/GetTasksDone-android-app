package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TagWithTasks(
    @Embedded val task: TagEntity,
    @Relation(
        parentColumn = "tagId",
        entityColumn = "taskId",
        associateBy = Junction(TagTaskCrossRef::class)
    )
    val tags: List<TaskEntity>
)

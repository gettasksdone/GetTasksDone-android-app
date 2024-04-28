package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.Entity

@Entity(primaryKeys = ["taskId", "tagId"])
data class TagTaskCrossRef(
    val taskId: Long,
    val tagId: Long
)

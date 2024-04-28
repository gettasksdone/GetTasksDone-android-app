package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["taskId", "tagId"])
data class TagTaskCrossRef(
    val taskId: Long,
    @ColumnInfo(index = true)
    val tagId: Long
)

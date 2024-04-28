package com.gettasksdone.gettasksdone.data.local.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["projectId", "tagId"])
data class TagProjectCrossRef(
    val projectId: Long,
    @ColumnInfo(index = true)
    val tagId: Long
)

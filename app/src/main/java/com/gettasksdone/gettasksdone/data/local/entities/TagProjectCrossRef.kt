package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.Entity

@Entity(primaryKeys = ["projectId", "tagId"])
data class TagProjectCrossRef(
    val projectId: Long,
    val tagId: Long
)

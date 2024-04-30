package com.gettasksdone.gettasksdone.data.local.entities

import androidx.annotation.WorkerThread
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gettasksdone.gettasksdone.data.layout.TagEM

/*
    {
        "id": 12345
        "nombre": "Mejora"
    }
*/
@Entity(tableName = "tag")
data class TagEntity(
    @PrimaryKey val tagId: Long,
    @ColumnInfo(name = "nombre") val nombre: String
){
    fun asExternalModel(): TagEM = TagEM(
        id = tagId,
        nombre = nombre
    )
}
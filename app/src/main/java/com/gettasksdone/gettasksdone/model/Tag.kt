package com.gettasksdone.gettasksdone.model

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.local.entities.TagEntity

/*
    {
        "id": 12345
        "nombre": "Mejora"
    }
*/
data class Tag(
    val id: Long,
    val nombre: String
){
    fun asEntity(): TagEntity = TagEntity(
        tagId = id,
        nombre = nombre
    )
}
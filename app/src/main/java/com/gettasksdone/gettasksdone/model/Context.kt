package com.gettasksdone.gettasksdone.model

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.local.entities.ContextEntity

/*
    {
        "id": 1234,
        "nombre": "Base de Datos"
    }
*/
data class Context(
    val id: Long,
    val nombre: String
){
    fun asEntity(): ContextEntity = ContextEntity(
        id = id,
        nombre = nombre
    )
}
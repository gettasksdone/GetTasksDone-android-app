package com.gettasksdone.gettasksdone.model

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.local.entities.CheckItemEntity

/*
    {
        "id": 1,
        "contenido": "Check item de prueba",
        "esta_marcado": false
    }
*/
data class CheckItem (
    val id: Long,
    val contenido: String,
    val esta_marcado: Boolean,
    val tareaId: Long
){
    fun asEntity(): CheckItemEntity = CheckItemEntity(
        id = id,
        contenido = contenido,
        estaMarcado = esta_marcado,
        taskId = tareaId
    )
}
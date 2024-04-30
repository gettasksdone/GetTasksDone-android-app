package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gettasksdone.gettasksdone.data.layout.CheckItemEM

/*
    {
        "id": 1,
        "contenido": "Check item de prueba",
        "esta_marcado": false
    }
*/
@Entity(tableName = "checkItem")
data class CheckItemEntity (
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "contenido") val contenido: String,
    @ColumnInfo(name = "esta_marcado") val estaMarcado: Boolean,
    @ColumnInfo(name = "taskId") val taskId: Long
){
    fun asExternalModel(): CheckItemEM = CheckItemEM(
        id = id,
        contenido = contenido,
        estaMarcado = estaMarcado
    )
}
package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class TaskEntity (
    @PrimaryKey val taskId: Long,
    @ColumnInfo(name = "titulo") val titulo: String,
    @ColumnInfo(name = "descripcion") val descripcion: String? = null,
    @ColumnInfo(name = "estado") val estado: String,
    @ColumnInfo(name = "prioridad") val prioridad: Int,
    @ColumnInfo(name = "contexto") val contexto: Long,
    @ColumnInfo(name = "creacion") val creacion: String? = null,
    @ColumnInfo(name = "vencimiento") val vencimiento: String? = null,
    @ColumnInfo(name = "projectId") val proyecto: Long
)
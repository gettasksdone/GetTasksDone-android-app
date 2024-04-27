package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class TaskEntity (
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "titulo") val titulo: String,
    @ColumnInfo(name = "descripcion") val descripcion: String,
    @ColumnInfo(name = "estado") val estado: String,
    @ColumnInfo(name = "prioridad") val prioridad: Int,
    //@Ignore val contexto: ContextEntity,//TODO - Investigar la definicion de relaciones entre objetos
    @ColumnInfo(name = "creacion") val creacion: String,
    @ColumnInfo(name = "vencimiento") val vencimiento: String,
    //@Ignore val checkItemEntities: List<CheckItemEntity> = emptyList(),//TODO - Investigar la definicion de relaciones entre objetos
    //@Ignore val notas: List<NoteEntity> = emptyList(),//TODO - Investigar la definicion de relaciones entre objetos
    //@Ignore val etiquetas: List<TagEntity> = emptyList()//TODO - Investigar la definicion de relaciones entre objetos
)
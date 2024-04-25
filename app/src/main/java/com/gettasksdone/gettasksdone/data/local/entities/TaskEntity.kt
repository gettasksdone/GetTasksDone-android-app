package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntity (
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "titulo") val titulo: String,
    @ColumnInfo(name = "descripcion") val descripcion: String,
    @ColumnInfo(name = "estado") val estado: String,
    @ColumnInfo(name = "prioridad") val prioridad: Int,
    val contexto: ContextEntity,//TODO - Investigar la definicion de relaciones entre objetos
    @ColumnInfo(name = "creacion") val creacion: String,
    @ColumnInfo(name = "vencimiento") val vencimiento: String,
    val checkItemEntities: List<CheckItemEntity> = emptyList(),//TODO - Investigar la definicion de relaciones entre objetos
    val notas: List<NoteEntity> = emptyList(),//TODO - Investigar la definicion de relaciones entre objetos
    val etiquetas: List<TagEntity> = emptyList()//TODO - Investigar la definicion de relaciones entre objetos
)
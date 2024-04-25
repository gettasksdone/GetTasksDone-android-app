package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProjectEntity (
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "descripcion") val descripcion: String,
    @ColumnInfo(name = "estado") val estado: String,
    @ColumnInfo(name = "inicio") val inicio: String,
    @ColumnInfo(name = "fin") val fin: String,
    val tareas: List<TaskEntity> = emptyList(), //TODO - Investigar la definicion de relaciones entre objetos
    val notas: List<NoteEntity> = emptyList(), //TODO - Investigar la definicion de relaciones entre objetos
    val etiquetas: List<TagEntity> = emptyList() //TODO - Investigar la definicion de relaciones entre objetos
)
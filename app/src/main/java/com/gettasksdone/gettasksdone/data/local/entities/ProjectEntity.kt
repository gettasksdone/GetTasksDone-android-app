package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "project")
data class ProjectEntity (
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "descripcion") val descripcion: String,
    @ColumnInfo(name = "estado") val estado: String,
    @ColumnInfo(name = "inicio") val inicio: String,
    @ColumnInfo(name = "fin") val fin: String,
    //@Ignore val tareas: List<TaskEntity> = emptyList(), //TODO - Investigar la definicion de relaciones entre objetos
    //@Ignore val notas: List<NoteEntity> = emptyList(), //TODO - Investigar la definicion de relaciones entre objetos
    //@Ignore val etiquetas: List<TagEntity> = emptyList() //TODO - Investigar la definicion de relaciones entre objetos
)
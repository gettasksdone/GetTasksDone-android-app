package com.gettasksdone.gettasksdone.data.local.entities

import androidx.annotation.WorkerThread
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gettasksdone.gettasksdone.data.layout.ProjectEM

@Entity(tableName = "project")
data class ProjectEntity (
    @PrimaryKey val projectId: Long,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "descripcion") val descripcion: String? = null,
    @ColumnInfo(name = "estado") val estado: String,
    @ColumnInfo(name = "inicio") val inicio: String? = null,
    @ColumnInfo(name = "fin") val fin: String? = null
){
    fun asExternalModel(): ProjectEM = ProjectEM(
        id = projectId,
        nombre = nombre,
        descripcion = descripcion,
        estado = estado,
        inicio = inicio,
        fin = fin
    )
}
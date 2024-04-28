package com.gettasksdone.gettasksdone.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "project")
data class ProjectEntity (
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "descripcion") val descripcion: String? = null,
    @ColumnInfo(name = "estado") val estado: String,
    @ColumnInfo(name = "inicio") val inicio: String? = null,
    @ColumnInfo(name = "fin") val fin: String? = null
)
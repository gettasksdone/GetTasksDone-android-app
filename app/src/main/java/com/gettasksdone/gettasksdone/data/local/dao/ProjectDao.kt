package com.gettasksdone.gettasksdone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.gettasksdone.gettasksdone.data.local.entities.ProjectEntity

@Dao
interface ProjectDao {
    @Query("SELECT * FROM ProjectEntity")
    fun getAll(): List<ProjectEntity>
    @Query("SELECT * FROM ProjectEntity WHERE id IN (:projectIds)")
    fun loadAllByIds(projectIds: LongArray): List<ProjectEntity>
    @Insert
    fun insertAll(vararg projects: ProjectEntity)
    @Delete
    fun delete(project: ProjectEntity)
}
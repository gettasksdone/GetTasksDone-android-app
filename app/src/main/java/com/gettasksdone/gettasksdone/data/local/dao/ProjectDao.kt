package com.gettasksdone.gettasksdone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.gettasksdone.gettasksdone.data.local.entities.ProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM project")
    fun getAll(): Flow<List<ProjectEntity>>
    @Query("SELECT * FROM project WHERE id=(:projectId)")
    fun loadById(projectId: Long): Flow<List<ProjectEntity>>
    @Insert
    suspend fun insertAll(vararg projects: ProjectEntity)
    @Upsert
    suspend fun upsert(project: ProjectEntity)
    @Delete
    suspend fun delete(project: ProjectEntity)
}
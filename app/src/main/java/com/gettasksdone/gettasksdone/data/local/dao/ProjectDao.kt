package com.gettasksdone.gettasksdone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.gettasksdone.gettasksdone.data.local.entities.ProjectEntity
import com.gettasksdone.gettasksdone.data.local.entities.ProjectWithNotes
import com.gettasksdone.gettasksdone.data.local.entities.ProjectWithTags
import com.gettasksdone.gettasksdone.data.local.entities.ProjectWithTasks
import com.gettasksdone.gettasksdone.data.local.entities.TaskWithCheckItems
import com.gettasksdone.gettasksdone.data.local.entities.TaskWithNotes
import com.gettasksdone.gettasksdone.data.local.entities.TaskWithTags
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM project")
    fun getAll(): Flow<List<ProjectEntity>>
    @Transaction
    @Query("SELECT * FROM project")
    fun getAllWithTasks(): Flow<List<ProjectWithTasks>>
    @Transaction
    @Query("SELECT * FROM project")
    fun getAllWithTags(): Flow<List<ProjectWithTags>>
    @Transaction
    @Query("SELECT * FROM project")
    fun getAllWithNotes(): Flow<List<ProjectWithNotes>>
    @Query("SELECT * FROM project WHERE id=(:projectId)")
    fun loadById(projectId: Long): Flow<List<ProjectEntity>>
    @Transaction
    @Query("SELECT * FROM project WHERE id=(:projectId)")
    fun loadByIdWithTasks(projectId: Long): Flow<List<ProjectWithTasks>>
    @Transaction
    @Query("SELECT * FROM project WHERE id=(:projectId)")
    fun loadByIdWithTags(projectId: Long): Flow<List<ProjectWithTags>>
    @Transaction
    @Query("SELECT * FROM project WHERE id=(:projectId)")
    fun loadByIdWithNotes(projectId: Long): Flow<List<ProjectWithNotes>>
    @Insert
    suspend fun insertAll(vararg projects: ProjectEntity)
    @Upsert
    suspend fun upsert(project: ProjectEntity)
    @Delete
    suspend fun delete(project: ProjectEntity)
}
package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.layout.ProjectEM
import com.gettasksdone.gettasksdone.data.local.dao.ProjectDao
import com.gettasksdone.gettasksdone.data.local.entities.ProjectEntity
import com.gettasksdone.gettasksdone.model.Project
import kotlinx.coroutines.flow.Flow

open class ProjectRepository(private val projectDao: ProjectDao) {
    @WorkerThread
    fun getAll(): Flow<List<ProjectEntity>>{
        return projectDao.getAll()
    }
    @WorkerThread
    fun get(project: Long): Flow<List<ProjectEntity>> {
        return projectDao.loadById(project)
    }

    @WorkerThread
    suspend fun upsert(project: ProjectEntity){
        projectDao.upsert(project)
    }
    @WorkerThread
    suspend fun delete(project: ProjectEntity){
        projectDao.delete(project)
    }
}
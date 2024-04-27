package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.local.dao.ProjectDao
import com.gettasksdone.gettasksdone.data.local.entities.ProjectEntity
import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val projectDao: ProjectDao) {
    val allCheckItems: Flow<List<ProjectEntity>> = projectDao.getAll()
    @WorkerThread
    fun get(project: Long){
        projectDao.loadById(project)
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
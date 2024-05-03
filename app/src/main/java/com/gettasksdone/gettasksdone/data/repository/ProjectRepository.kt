package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.data.layout.ProjectEM
import com.gettasksdone.gettasksdone.data.local.dao.ProjectDao
import com.gettasksdone.gettasksdone.data.local.entities.ProjectEntity
import com.gettasksdone.gettasksdone.data.mapper.toDomain
import com.gettasksdone.gettasksdone.data.mapper.toEntity
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.model.Context
import com.gettasksdone.gettasksdone.model.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

open class ProjectRepository(
    private val api: ApiService,
    private val jwtHelper: JwtHelper,
    private val projectDao: ProjectDao) {

    suspend fun getAll(): List<Project>{
        return withContext(Dispatchers.IO){
            var localProjects = projectDao.getAll()
            if(localProjects.isEmpty()){
                val remoteProjects = getProjectsRemote()
                remoteProjects.forEach {
                    projectDao.insertAll(it.toEntity())
                }
                localProjects = projectDao.getAll()
            }
            localProjects.map { it.toDomain() }
        }
    }

    private suspend fun getProjectsRemote(): List<Project>{
        return withContext(Dispatchers.IO){
            try{
                val authHeader = "Bearer ${jwtHelper.getToken()}"
                val call = api.getProjects(authHeader)
                val response = call.execute()
                if(response.isSuccessful){
                    response.body() ?: emptyList()
                }else{
                    emptyList()
                }
            }catch (e: Exception){
                emptyList()
            }
        }
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
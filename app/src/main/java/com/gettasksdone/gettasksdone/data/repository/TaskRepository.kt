package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.data.layout.TaskEM
import com.gettasksdone.gettasksdone.data.local.dao.CheckItemDao
import com.gettasksdone.gettasksdone.data.local.dao.TaskDao
import com.gettasksdone.gettasksdone.data.local.entities.CheckItemEntity
import com.gettasksdone.gettasksdone.data.local.entities.TaskEntity
import com.gettasksdone.gettasksdone.data.mapper.toDomain
import com.gettasksdone.gettasksdone.data.mapper.toEntity
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class TaskRepository(
    private val api: ApiService,
    private val jwtHelper: JwtHelper,
    private val taskDao: TaskDao
) {
    suspend fun getAll(): List<Task>{
        return withContext(Dispatchers.IO) {
            var localTasks = taskDao.getAll()

            if(localTasks.isEmpty()) {
                val remoteTasks = getTasksRemote()
                remoteTasks.forEach{
                    taskDao.insertAll(it.toEntity())
                }
                localTasks = taskDao.getAll()
            }

            localTasks.map{ it.toDomain() }
        }
    }

    private suspend fun getTasksRemote(): List<Task> {
        return withContext(Dispatchers.IO) {
            try {
                val authHeader = "Bearer ${jwtHelper.getToken()}"
                val call = api.getTasks(authHeader)
                val response = call.execute() // Realizar la llamada de manera síncrona
                if (response.isSuccessful) {
                    response.body() ?: emptyList() // Devolver la lista de tareas si la respuesta no es nula
                } else {
                    emptyList() // Devolver una lista vacía si la respuesta no es exitosa
                }
            } catch (e: Exception) {
                emptyList() // Manejar cualquier excepción y devolver una lista vacía
            }
        }
    }

    @WorkerThread
    fun get(task: Long): Flow<List<TaskEntity>> {
        return taskDao.loadById(task)
    }

    @WorkerThread
    suspend fun upsert(task: TaskEntity){
        taskDao.upsert(task)
    }
    @WorkerThread
    suspend fun delete(task: TaskEntity){
        taskDao.delete(task)
    }
}

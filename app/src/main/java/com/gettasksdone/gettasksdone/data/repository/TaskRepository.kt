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

    private fun getTasksRemote(): List<Task> {
        try {
            val authHeader = "Bearer ${jwtHelper.getToken()}"
            val call = api.getTasks(authHeader)
            var tasks = emptyList<Task>()
            call.enqueue(object : Callback<List<Task>> {
                override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                    if (response.isSuccessful) {
                        val tasksFromApi = response.body()
                        // Filtrar las tareas para excluir aquellas con el estado "completado"
                        if (tasksFromApi != null) {
                            tasks = tasksFromApi
                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                    // Manejar errores de llamada a la API
                    // Puedes mostrar un mensaje de error en la interfaz de usuario o manejarlo de otra manera
                    t.printStackTrace()
                }
            })
            return tasks
        }catch (e: Exception){
            return emptyList()
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

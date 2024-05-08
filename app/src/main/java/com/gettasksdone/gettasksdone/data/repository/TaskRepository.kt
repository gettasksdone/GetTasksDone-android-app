package com.gettasksdone.gettasksdone.data.repository

import android.util.Log
import android.widget.Toast
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
    private val api: ApiService?,
    private val jwtHelper: JwtHelper,
    private val taskDao: TaskDao
) {
    suspend fun getAll(): List<Task>{
        return withContext(Dispatchers.IO) {
            //Hay que darle prioridad a la informacion que venga desde red
            var remoteTasks = emptyList<Task>()
            try {
                val authHeader = "Bearer ${jwtHelper.getToken()}"
                val call = api?.getTasks(authHeader)
                val response = call?.execute() // Realizar la llamada de manera síncrona
                if (response != null) {
                    if (response.isSuccessful) {
                        taskDao.deleteAll()
                        remoteTasks = response.body() ?: emptyList() // Devolver la lista de tareas si la respuesta no es nula
                        remoteTasks.forEach{
                            taskDao.insertAll(it.toEntity())
                        }
                    }
                }
            } catch (e: Exception) { //No hay conexión con la red, por lo que nos limitamos a recuperar la información de la base de datos local
                Log.w("TaskRepository", "No hay conexión con la red")
            }

            if (remoteTasks.isNotEmpty()) {
                return@withContext remoteTasks
            }

            val localTasks: List<TaskEntity> = taskDao.getAll()
            return@withContext localTasks.map { it.toDomain() }
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

package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.local.dao.CheckItemDao
import com.gettasksdone.gettasksdone.data.local.dao.TaskDao
import com.gettasksdone.gettasksdone.data.local.entities.CheckItemEntity
import com.gettasksdone.gettasksdone.data.local.entities.TaskEntity
import com.gettasksdone.gettasksdone.model.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    val allCheckItems: Flow<List<TaskEntity>> = taskDao.getAll()
    @WorkerThread
    fun get(task: Long){
        taskDao.loadById(task)
    }

    @WorkerThread
    suspend fun upsert(task: TaskEntity){
        taskDao.upsert(task)
    }
    @WorkerThread
    suspend fun delete(task: TaskEntity){
        taskDao.delete(task)
    }
    @WorkerThread
    fun Task.asEntity() = TaskEntity(
        id = id,
        titulo = titulo,
        descripcion = descripcion,
        estado = estado,
        creacion = creacion,
        vencimiento = vencimiento,
        prioridad = prioridad,
        contexto = contextoId,
        proyecto = proyectoId
    )
}
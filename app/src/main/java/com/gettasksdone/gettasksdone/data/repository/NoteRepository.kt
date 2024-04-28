package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.layout.NoteEM
import com.gettasksdone.gettasksdone.data.local.dao.NoteDao
import com.gettasksdone.gettasksdone.data.local.entities.NoteEntity
import com.gettasksdone.gettasksdone.model.Note
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {
    val allCheckItems: Flow<List<NoteEntity>> = noteDao.getAll()
    @WorkerThread
    fun get(note: Long){
        noteDao.loadById(note)
    }

    @WorkerThread
    suspend fun upsert(note: NoteEntity){
        noteDao.upsert(note)
    }
    @WorkerThread
    suspend fun delete(note: NoteEntity){
        noteDao.delete(note)
    }
    @WorkerThread
    fun Note.asEntity() = NoteEntity(
        id = id,
        contenido = contenido,
        creacion = creacion,
        projectId = proyectoId,
        taskId = tareaId
    )
    @WorkerThread
    fun NoteEntity.asExternalModel() = NoteEM(
        id = id,
        contenido = contenido,
        creacion = creacion
    )
}
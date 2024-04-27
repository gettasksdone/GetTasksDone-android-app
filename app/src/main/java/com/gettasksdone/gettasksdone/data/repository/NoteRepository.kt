package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.local.dao.NoteDao
import com.gettasksdone.gettasksdone.data.local.entities.NoteEntity
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
}
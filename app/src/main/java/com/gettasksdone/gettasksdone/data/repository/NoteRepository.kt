package com.gettasksdone.gettasksdone.data.repository

import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.layout.NoteEM
import com.gettasksdone.gettasksdone.data.local.dao.NoteDao
import com.gettasksdone.gettasksdone.data.local.entities.NoteEntity
import com.gettasksdone.gettasksdone.model.Note
import kotlinx.coroutines.flow.Flow

open class NoteRepository(private val noteDao: NoteDao) {
    @WorkerThread
    fun getAll(): Flow<List<NoteEntity>>{
        return noteDao.getAll()
    }
    @WorkerThread
    fun get(note: Long): Flow<List<NoteEntity>> {
        return noteDao.loadById(note)
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
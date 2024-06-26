package com.gettasksdone.gettasksdone.data.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.data.layout.NoteEM
import com.gettasksdone.gettasksdone.data.local.dao.NoteDao
import com.gettasksdone.gettasksdone.data.local.entities.NoteEntity
import com.gettasksdone.gettasksdone.data.mapper.toDomain
import com.gettasksdone.gettasksdone.data.mapper.toEntity
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.model.Context
import com.gettasksdone.gettasksdone.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

open class NoteRepository(
    private val api: ApiService?,
    private val jwtHelper: JwtHelper,
    private val noteDao: NoteDao) {
    suspend fun getAll(): List<Note>{
        return withContext(Dispatchers.IO){
            try{
                val authHeader = "Bearer ${jwtHelper.getToken()}"
                val call = api?.getNotes(authHeader)
                val response = call?.execute()
                if (response != null) {
                    if(response.isSuccessful){
                        noteDao.deleteAll()
                        val remoteNotes = response.body() ?: emptyList()
                        remoteNotes.forEach{
                            noteDao.insertAll(it.toEntity())
                        }
                    }
                }
            }catch (e: Exception){
                Log.w("NoteRepository", "No hay conexión con la red")
            }
            val localNotes = noteDao.getAll()
            localNotes.map { it.toDomain() }
        }
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
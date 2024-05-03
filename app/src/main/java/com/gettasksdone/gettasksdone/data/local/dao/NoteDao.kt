package com.gettasksdone.gettasksdone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.gettasksdone.gettasksdone.data.local.entities.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getAll(): List<NoteEntity>
    @Query("SELECT * FROM note WHERE id=(:noteId)")
    fun loadById(noteId: Long): Flow<List<NoteEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg notes: NoteEntity)
    @Upsert
    suspend fun upsert(note: NoteEntity)
    @Delete
    suspend fun delete(note: NoteEntity)
}
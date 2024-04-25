package com.gettasksdone.gettasksdone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.gettasksdone.gettasksdone.data.local.entities.NoteEntity

@Dao
interface NoteDao {
    @Query("SELECT * FROM NoteEntity")
    fun getAll(): List<NoteEntity>
    @Query("SELECT * FROM NoteEntity WHERE id IN (:noteIds)")
    fun loadAllByIds(noteIds: LongArray): List<NoteEntity>
    @Insert
    fun insertAll(vararg notes: NoteEntity)
    @Delete
    fun delete(note: NoteEntity)
}
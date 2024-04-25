package com.gettasksdone.gettasksdone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.gettasksdone.gettasksdone.data.local.entities.ContextEntity

@Dao
interface ContextDao {
    @Query("SELECT * FROM ContextEntity")
    fun getAll(): List<ContextEntity>
    @Query("SELECT * FROM ContextEntity WHERE id IN (:contextIds)")
    fun loadAllByIds(contextIds: LongArray): List<ContextEntity>
    @Insert
    fun insertAll(vararg contexts: ContextEntity)
    @Delete
    fun delete(context: ContextEntity)
}
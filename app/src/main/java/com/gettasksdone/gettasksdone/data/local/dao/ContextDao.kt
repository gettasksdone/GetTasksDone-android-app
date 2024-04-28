package com.gettasksdone.gettasksdone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.gettasksdone.gettasksdone.data.local.entities.ContextEntity
import com.gettasksdone.gettasksdone.data.local.entities.ContextWithTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface ContextDao {
    @Query("SELECT * FROM context")
    fun getAll(): Flow<List<ContextEntity>>
    @Transaction
    @Query("SELECT * FROM context")
    fun getAllWithTasks(): Flow<List<ContextWithTasks>>
    @Transaction
    @Query("SELECT * FROM context WHERE id=(:contextId)")
    fun loadByIdWithTasks(contextId: Long): Flow<List<ContextWithTasks>>
    @Query("SELECT * FROM context WHERE id=(:contextId)")
    fun loadById(contextId: Long): Flow<List<ContextEntity>>
    @Insert
    suspend fun insertAll(vararg contexts: ContextEntity)
    @Upsert
    suspend fun upsert(context: ContextEntity)
    @Delete
    suspend fun delete(context: ContextEntity)
}
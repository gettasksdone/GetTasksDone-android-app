package com.gettasksdone.gettasksdone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.gettasksdone.gettasksdone.data.local.entities.TagEntity
import com.gettasksdone.gettasksdone.data.local.entities.TagWithProjects
import com.gettasksdone.gettasksdone.data.local.entities.TagWithTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Query("SELECT * FROM tag")
    fun getAll(): Flow<List<TagEntity>>
    @Transaction
    @Query("SELECT * FROM tag")
    fun getAllWithTasks(): Flow<List<TagWithTasks>>
    @Transaction
    @Query("SELECT * FROM tag")
    fun getAllWithProjects(): Flow<List<TagWithProjects>>
    @Query("SELECT * FROM tag WHERE id IN (:tagId)")
    fun loadById(tagId: Long): Flow<List<TagEntity>>
    @Transaction
    @Query("SELECT * FROM tag WHERE id=(:tagId)")
    fun loadByIdWithTasks(tagId: Long): Flow<List<TagWithTasks>>
    @Transaction
    @Query("SELECT * FROM tag WHERE id=(:tagId)")
    fun loadByIdWithProjects(tagId: Long): Flow<List<TagWithProjects>>
    @Insert
    suspend fun insertAll(vararg tags: TagEntity)
    @Upsert
    suspend fun upsert(tag: TagEntity)
    @Delete
    suspend fun delete(tag: TagEntity)
}
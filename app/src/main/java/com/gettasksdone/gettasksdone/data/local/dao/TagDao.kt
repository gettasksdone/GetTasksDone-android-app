package com.gettasksdone.gettasksdone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
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
    fun getAll(): List<TagEntity>
    @Transaction
    @Query("SELECT * FROM tag")
    fun getAllWithTasks(): Flow<List<TagWithTasks>>
    @Transaction
    @Query("SELECT * FROM tag")
    fun getAllWithProjects(): Flow<List<TagWithProjects>>
    @Query("SELECT * FROM tag WHERE tagId IN (:tagId)")
    fun loadById(tagId: Long): Flow<List<TagEntity>>
    @Transaction
    @Query("SELECT * FROM tag WHERE tagId=(:tagId)")
    fun loadByIdWithTasks(tagId: Long): Flow<List<TagWithTasks>>
    @Transaction
    @Query("SELECT * FROM tag WHERE tagId=(:tagId)")
    fun loadByIdWithProjects(tagId: Long): Flow<List<TagWithProjects>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg tags: TagEntity)
    @Upsert
    suspend fun upsert(tag: TagEntity)
    @Delete
    suspend fun delete(tag: TagEntity)
    @Query("DELETE FROM tag")
    suspend fun deleteAll()
}
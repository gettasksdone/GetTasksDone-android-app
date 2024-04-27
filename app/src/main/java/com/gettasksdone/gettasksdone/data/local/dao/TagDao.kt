package com.gettasksdone.gettasksdone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.gettasksdone.gettasksdone.data.local.entities.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Query("SELECT * FROM tag")
    fun getAll(): Flow<List<TagEntity>>
    @Query("SELECT * FROM tag WHERE id IN (:tagId)")
    fun loadById(tagId: Long): Flow<List<TagEntity>>
    @Insert
    suspend fun insertAll(vararg tags: TagEntity)
    @Upsert
    suspend fun upsert(tag: TagEntity)
    @Delete
    suspend fun delete(tag: TagEntity)
}
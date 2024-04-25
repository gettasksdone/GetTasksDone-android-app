package com.gettasksdone.gettasksdone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.gettasksdone.gettasksdone.data.local.entities.TagEntity

@Dao
interface TagDao {
    @Query("SELECT * FROM TagEntity")
    fun getAll(): List<TagEntity>
    @Query("SELECT * FROM TagEntity WHERE id IN (:tagIds)")
    fun loadAllByIds(tagIds: LongArray): List<TagEntity>
    @Insert
    fun insertAll(vararg tags: TagEntity)
    @Delete
    fun delete(tag: TagEntity)
}
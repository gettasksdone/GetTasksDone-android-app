package com.gettasksdone.gettasksdone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.gettasksdone.gettasksdone.data.local.entities.CheckItemEntity

@Dao
interface CheckItemDao {
    @Query("SELECT * FROM CheckItemEntity")
    fun getAll(): List<CheckItemEntity>
    @Query("SELECT * FROM CheckItemEntity WHERE id IN (:checkIds)")
    fun loadAllByIds(checkIds: LongArray): List<CheckItemEntity>
    @Insert
    fun insertAll(vararg checks: CheckItemEntity)
    @Delete
    fun delete(check: CheckItemEntity)
}
package com.gettasksdone.gettasksdone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.gettasksdone.gettasksdone.data.local.entities.CheckItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckItemDao {
    @Query("SELECT * FROM checkItem")
    fun getAll(): List<CheckItemEntity>
    @Query("SELECT * FROM checkItem WHERE id=(:checkId)")
    fun loadById(checkId: Long): Flow<List<CheckItemEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg checks: CheckItemEntity)
    @Upsert
    suspend fun upsert(check: CheckItemEntity)
    @Delete
    suspend fun delete(check: CheckItemEntity)
}
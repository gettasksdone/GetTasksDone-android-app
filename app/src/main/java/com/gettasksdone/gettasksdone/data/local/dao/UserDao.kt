package com.gettasksdone.gettasksdone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.gettasksdone.gettasksdone.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): Flow<List<UserEntity>>
    @Query("SELECT * FROM user WHERE id=(:userId)")
    fun loadById(userId: Long): Flow<List<UserEntity>>
    @Insert
    suspend fun insertAll(vararg users: UserEntity)
    @Upsert
    suspend fun upsert(user: UserEntity)
    @Delete
    suspend fun delete(user: UserEntity)
}
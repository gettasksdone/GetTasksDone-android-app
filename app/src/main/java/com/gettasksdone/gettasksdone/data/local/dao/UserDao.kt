package com.gettasksdone.gettasksdone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.gettasksdone.gettasksdone.data.local.entities.UserAndUserInfo
import com.gettasksdone.gettasksdone.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<UserEntity>
    @Transaction
    @Query("SELECT * FROM user")
    fun getAllWithData(): Flow<List<UserAndUserInfo>>
    @Transaction
    @Query("SELECT * FROM user WHERE id=(:userId)")
    fun loadByIdWithData(userId: Long): Flow<List<UserAndUserInfo>>
    @Query("SELECT * FROM user WHERE id=(:userId)")
    fun loadById(userId: Long): Flow<List<UserEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg users: UserEntity)
    @Upsert
    suspend fun upsert(user: UserEntity)
    @Delete
    suspend fun delete(user: UserEntity)
    @Query("DELETE FROM user")
    suspend fun deleteAll()
}
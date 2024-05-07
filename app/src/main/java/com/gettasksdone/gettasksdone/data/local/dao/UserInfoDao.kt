package com.gettasksdone.gettasksdone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.gettasksdone.gettasksdone.data.local.entities.UserInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserInfoDao {
    @Query("SELECT * FROM userInfo")
    fun getAll(): List<UserInfoEntity>
    @Query("SELECT * FROM userInfo WHERE id=(:userDataId)")
    fun loadById(userDataId: Long): Flow<List<UserInfoEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg userDataIds: UserInfoEntity)
    @Upsert
    suspend fun upsert(userData: UserInfoEntity)
    @Delete
    suspend fun delete(userData: UserInfoEntity)
    @Query("DELETE FROM userInfo")
    suspend fun deleteAll()
}
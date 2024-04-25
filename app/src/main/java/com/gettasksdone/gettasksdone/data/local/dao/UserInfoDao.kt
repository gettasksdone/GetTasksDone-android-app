package com.gettasksdone.gettasksdone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.gettasksdone.gettasksdone.data.local.entities.UserInfoEntity

@Dao
interface UserInfoDao {
    @Query("SELECT * FROM UserInfoEntity")
    fun getAll(): List<UserInfoEntity>
    @Query("SELECT * FROM UserInfoEntity WHERE id IN (:userDataIds)")
    fun loadAllByIds(userDataIds: LongArray): List<UserInfoEntity>
    @Insert
    fun insertAll(vararg userDataIds: UserInfoEntity)
    @Delete
    fun delete(userData: UserInfoEntity)
}
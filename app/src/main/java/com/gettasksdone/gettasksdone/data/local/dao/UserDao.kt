package com.gettasksdone.gettasksdone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.gettasksdone.gettasksdone.data.local.entities.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM UserEntity")
    fun getAll(): List<UserEntity>
    @Query("SELECT * FROM UserEntity WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: LongArray): List<UserEntity>
    @Insert
    fun insertAll(vararg users: UserEntity)
    @Delete
    fun delete(user: UserEntity)
}
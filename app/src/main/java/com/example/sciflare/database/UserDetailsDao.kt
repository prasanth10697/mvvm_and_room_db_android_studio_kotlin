package com.example.sciflare.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDetailsDao {
    @Query("SELECT * FROM user_details")
    fun getAllStories(): List<UserDetailsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStories(stories: List<UserDetailsEntity>)
}
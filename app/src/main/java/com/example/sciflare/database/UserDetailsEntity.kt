package com.example.sciflare.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_details")
data class UserDetailsEntity(
    @PrimaryKey val id: Int,
    val gender:String,
    val email: String,
    val name: String
)

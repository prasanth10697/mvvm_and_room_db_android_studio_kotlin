package com.example.sciflare.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserDetailsEntity::class], version = 1)
abstract class UserLocalDatabase : RoomDatabase() {
    abstract fun storyDao(): UserDetailsDao

    companion object {
        @Volatile
        private var INSTANCE: UserLocalDatabase? = null

        fun getDatabase(context: Context): UserLocalDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserLocalDatabase::class.java,
                    "user_details"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
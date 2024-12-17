package com.example.meetingdesu.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.meetingdesu.database.room.dao.MeetingDao
import com.example.meetingdesu.database.room.entity.Meeting

@Database(entities = [Meeting::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun meetingDao(): MeetingDao

    companion object {
        private const val NAME = "app-database"

        /**
         * As we need only one instance of db in our app will use to store
         * This is to avoid memory leaks in android when there exist multiple instances of db
         */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        NAME
                    ).build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
package com.example.meetingdesu.database.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.meetingdesu.database.room.entity.Meeting
import kotlinx.coroutines.flow.Flow

@Dao
interface MeetingDao {
    @Query("SELECT * FROM meetings")
    fun getAll(): Flow<List<Meeting>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(meeting: Meeting)

    @Delete
    suspend fun delete(meeting: Meeting)
}
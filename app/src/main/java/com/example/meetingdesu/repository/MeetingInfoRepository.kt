package com.example.meetingdesu.repository

import com.example.meetingdesu.database.room.dao.MeetingDao
import com.example.meetingdesu.database.room.entity.Meeting
import kotlinx.coroutines.flow.Flow

interface IMeetingInfoRepository {
    fun getAll(): Flow<List<Meeting>>
    suspend fun upsert(meeting: Meeting)
    suspend fun delete(meeting: Meeting)
}

class MeetingInfoRepository(
    private val meetingDao : MeetingDao
) : IMeetingInfoRepository {
    override fun getAll(): Flow<List<Meeting>> {
        return meetingDao.getAll()
    }

    override suspend fun upsert(meeting: Meeting) {
        meetingDao.upsert(meeting)
    }

    override suspend fun delete(meeting: Meeting) {
        meetingDao.delete(meeting)
    }
}
package com.example.meetingdesu.database.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.meetingdesu.extension.DateTimeFormatterType
import com.example.meetingdesu.extension.toLocalTime
import com.example.meetingdesu.extension.toString
import java.time.LocalTime

@Entity(tableName = "meetings")
data class Meeting(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val doToday: Boolean,
    val startTime: String,
    val endTime: String,
    val willSpeak: Boolean,
) {

    fun startTimeLocalTime(): LocalTime {
        return startTime.toLocalTime(formatType)
    }

    fun endTimeLocalTime(): LocalTime {
        return endTime.toLocalTime(formatType)
    }

    fun changeStartTime(newStartTime: LocalTime): Meeting {
        return this.copy(
            startTime = newStartTime.toString(formatType),
        )
    }

    fun changeEndTime(newEndTime: LocalTime): Meeting {
        return this.copy(
            endTime = newEndTime.toString(formatType),
        )
    }

    companion object {
        val formatType = DateTimeFormatterType.HHmm
        fun create(
            doToday: Boolean,
            startTime: LocalTime,
            endTime: LocalTime,
            willSpeak: Boolean,
        ): Meeting {
            return Meeting(
                doToday = doToday,
                startTime = startTime.toString(formatType),
                endTime = endTime.toString(formatType),
                willSpeak = willSpeak,
            )
        }
    }
}
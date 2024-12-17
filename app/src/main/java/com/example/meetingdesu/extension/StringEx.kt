package com.example.meetingdesu.extension

import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun String.toLocalTime(type: DateTimeFormatterType): LocalTime {
    val formatter = DateTimeFormatter.ofPattern(type.pattern)
    return LocalTime.parse(this, formatter)
}
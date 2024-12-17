package com.example.meetingdesu.extension

import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun LocalTime.toString(type: DateTimeFormatterType): String {
    val formatter = DateTimeFormatter.ofPattern(type.pattern)
    return this.format(formatter)
}


package com.example.meetingdesu.extension

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerState.toHHmm(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return LocalTime.of(hour, minute).format(formatter)
}
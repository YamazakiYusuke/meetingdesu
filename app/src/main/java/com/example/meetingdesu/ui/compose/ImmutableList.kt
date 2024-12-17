package com.example.meetingdesu.ui.compose

import androidx.compose.runtime.Immutable

@Immutable
class ImmutableList<T>(private val value: List<T>) : List<T> by value {
    override fun toString(): String {
        return value.toString()
    }
}

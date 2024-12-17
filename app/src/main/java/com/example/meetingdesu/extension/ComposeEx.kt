package com.example.meetingdesu.extension

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// dp(Dp) → sp(TextUnit)
@Composable
internal fun Dp.toSp(): TextUnit {
    return (this.value / LocalDensity.current.fontScale).sp
}

@Composable
fun Modifier.debouncedClickable(
    debounceTime: Long = 500L,
    onClick: () -> Unit
): Modifier {
    var isClickable by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    return this.then(
        Modifier.clickable(enabled = isClickable) {
            if (isClickable) {
                onClick()
                isClickable = false
                coroutineScope.launch {
                    delay(debounceTime)
                    isClickable = true
                }
            }
        }
    )
}
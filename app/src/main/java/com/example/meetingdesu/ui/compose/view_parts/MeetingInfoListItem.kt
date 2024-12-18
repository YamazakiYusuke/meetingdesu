package com.example.meetingdesu.ui.compose.view_parts

import android.content.res.Configuration
import androidx.annotation.ColorRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VoiceOverOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.meetingdesu.R
import com.example.meetingdesu.extension.debouncedClickable
import com.example.meetingdesu.extension.toSp
import com.example.meetingdesu.ui.theme.MeetingDesuTheme
import com.example.meetingdesu.ui.theme.MeetingInfoListItemBackground
import com.example.meetingdesu.ui.theme.MeetingInfoListItemTextOff
import com.example.meetingdesu.ui.theme.MeetingInfoListItemTextOn
import java.time.LocalTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MeetingInfoListItem(
    data: MeetingInfoListItemModel,
    modifier: Modifier = Modifier,
    switchDoToday: (id: Int, newValue: Boolean) -> Unit,
    onTapStartTime: (id: Int) -> Unit,
    onTapEndTime: (id: Int) -> Unit,
    switchWillSpeak: (id: Int, newValue: Boolean) -> Unit,
    onLongClickListItem: (id: Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(25.dp))
            .background(MeetingInfoListItemBackground)
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    onLongClickListItem(data.id)
                }
            )
    ) {
        Column(
            modifier = modifier
                .padding(16.dp),
        ) {
            val fontWeight = if (data.doToday) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            }
            val color = if (data.doToday) {
                MeetingInfoListItemTextOn
            } else {
                MeetingInfoListItemTextOff
            }
            MeetingTime(
                startTime = data.startTime,
                endTime = data.endTime,
                fontWeight = fontWeight,
                textColor = color,
                onTapStartTime = {
                    onTapStartTime(data.id)
                },
                onTapEndTime = {
                    onTapEndTime(data.id)
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1F))
                SpeakIcon(
                    willSpeak = data.willSpeak,
                    color = color,
                    switchWillSpeak = { newValue ->
                        switchWillSpeak(data.id, newValue)
                    }
                )
                Spacer(modifier = Modifier.width(20.dp))
                Switch(
                    checked = data.doToday,
                    onCheckedChange = { newValue ->
                        switchDoToday(data.id, newValue)
                    }
                )
            }
        }
    }
}

@Composable
private fun MeetingTime(
    startTime: String,
    endTime: String,
    fontWeight: FontWeight,
    textColor: Color,
    onTapStartTime: () -> Unit = { },
    onTapEndTime: () -> Unit = { },
) {
    Row {
        Text(
            text = startTime,
            fontSize = 38.dp.toSp(),
            color = textColor,
            fontWeight = fontWeight,
            modifier = Modifier.debouncedClickable {
                onTapStartTime()
            }
        )
        Text(
            text = "~",
            fontSize = 38.dp.toSp(),
            color = textColor,
            fontWeight = fontWeight,
        )
        Text(
            text = endTime,
            fontSize = 38.dp.toSp(),
            color = textColor,
            fontWeight = fontWeight,
            modifier = Modifier.debouncedClickable {
                onTapEndTime()
            }
        )
    }
}

@Composable
private fun SpeakIcon(
    willSpeak: Boolean,
    color: Color,
    switchWillSpeak: (Boolean) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .debouncedClickable {
                switchWillSpeak(!willSpeak)
            }
    ) {
        val imageVector = if (willSpeak) {
            Icons.Default.RecordVoiceOver
        } else {
            Icons.Default.VoiceOverOff
        }
        Icon(
            imageVector = imageVector,
            contentDescription = "speak icon",
            tint = color
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MeetingInfoListItemPreview() {
    MeetingDesuTheme {
        Surface {
            MeetingInfoListItem(
                data = MeetingInfoListItemModel(
                    id = 1,
                    doToday = false,
                    startTime = "10:00",
                    endTime = "10:30",
                    willSpeak = false,
                ),
                switchDoToday = { _: Int, _: Boolean -> },
                onTapStartTime = {},
                onTapEndTime = {},
                switchWillSpeak = { _: Int, _: Boolean -> },
                onLongClickListItem = {}
            )
        }
    }
}

data class MeetingInfoListItemModel(
    val id: Int,
    val doToday: Boolean,
    val startTime: String,
    val endTime: String,
    val willSpeak: Boolean,
)
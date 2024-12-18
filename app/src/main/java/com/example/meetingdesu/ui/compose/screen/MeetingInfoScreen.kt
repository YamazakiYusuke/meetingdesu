package com.example.meetingdesu.ui.compose.screen

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.meetingdesu.MainActivity
import com.example.meetingdesu.extension.debouncedClickable
import com.example.meetingdesu.ui.compose.ImmutableList
import com.example.meetingdesu.ui.compose.dialog.DeleteAlertDialog
import com.example.meetingdesu.ui.compose.dialog.TimePickerWithDialog
import com.example.meetingdesu.ui.compose.viewModel.MeetingInfoEvent
import com.example.meetingdesu.ui.compose.viewModel.MeetingInfoViewModel
import com.example.meetingdesu.ui.compose.view_parts.MeetingInfoListItem
import com.example.meetingdesu.ui.compose.view_parts.MeetingInfoListItemModel
import com.example.meetingdesu.ui.theme.MeetingDesuTheme
import com.example.meetingdesu.ui.theme.MeetingInfoScreenBackground
import com.example.meetingdesu.ui.theme.MeetingInfoScreenButton
import com.example.meetingdesu.ui.theme.MeetingInfoScreenButtonIcon
import java.time.LocalTime

@Composable
fun MeetingInfoScreen(
    viewModel: MeetingInfoViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    state.events.firstOrNull()?.let { event ->
        when (event) {
            is MeetingInfoEvent.Error -> {
                // TODO
            }

            is MeetingInfoEvent.ShowTimePickerDialogToEditStart -> {
                TimePickerWithDialog(
                    initialTime = event.initTime,
                    onConfirmation = { newStartTime ->
                        viewModel.changeStartTime(event.id, newStartTime)
                        viewModel.consumeEvent(event)
                    },
                    onDismissRequest = {
                        viewModel.consumeEvent(event)
                    }
                )
            }

            is MeetingInfoEvent.ShowTimePickerDialogToEditEnd -> {
                TimePickerWithDialog(
                    initialTime = event.initTime,
                    onConfirmation = { newEndTime ->
                        viewModel.changeEndTime(event.id, newEndTime)
                        viewModel.consumeEvent(event)
                    },
                    onDismissRequest = {
                        viewModel.consumeEvent(event)
                    }
                )
            }

            MeetingInfoEvent.ShowTimePickerDialogToCreateNew -> {
                TimePickerWithDialog(
                    initialTime = LocalTime.now().plusHours(1).withMinute(0),
                    onConfirmation = { startTime ->
                        viewModel.createMeeting(startTime)
                        viewModel.consumeEvent(event)
                    },
                    onDismissRequest = {
                        viewModel.consumeEvent(event)
                    }
                )
            }

            is MeetingInfoEvent.ShowAlertDialogToDeleteMeeting -> {
                DeleteAlertDialog(
                    name = event.meetingName,
                    onConfirmation = {
                        viewModel.deleteMeeting(event.id)
                        viewModel.consumeEvent(event)
                    },
                    onDismissRequest = {
                        viewModel.consumeEvent(event)
                    },
                )
            }

            is MeetingInfoEvent.SendMeetingInfo -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, event.text)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                val activity = LocalContext.current as MainActivity
                activity.startActivity(shareIntent)
                viewModel.consumeEvent(event)
            }
        }
    }


    MeetingInfoScreenContent(
        modifier = modifier,
        listItems = ImmutableList(state.listItems),
        switchDoToday = { id, newValue -> viewModel.changeDoToday(id, newValue) },
        onTapStartTime = { id -> viewModel.showTimerPickerDialogToEditStart(id) },
        onTapEndTime = { id -> viewModel.showTimerPickerDialogToEditEnd(id) },
        onTapCreateNew = { viewModel.showTimerPickerDialogToCreateNew() },
        onTapDeleteMeeting = { id -> viewModel.showAlertDialogToDeleteMeeting(id) },
        switchWillSpeak = { id, newValue -> viewModel.changeWillSpeak(id, newValue) },
        onTapSendButton = { viewModel.sendMeetingsInfo() }
    )
}

@Composable
fun MeetingInfoScreenContent(
    listItems: ImmutableList<MeetingInfoListItemModel>,
    modifier: Modifier = Modifier,
    switchDoToday: (id: Int, newValue: Boolean) -> Unit,
    onTapStartTime: (id: Int) -> Unit,
    onTapEndTime: (id: Int) -> Unit,
    onTapCreateNew: () -> Unit,
    onTapDeleteMeeting: (id: Int) -> Unit,
    switchWillSpeak: (id: Int, newValue: Boolean) -> Unit,
    onTapSendButton: () -> Unit,
) {
    Box(
        modifier = Modifier.background(MeetingInfoScreenBackground),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(top = 10.dp, bottom = 100.dp)
            ) {
                items(listItems.size) { index ->
                    val item = listItems[index]
                    MeetingInfoListItem(
                        data = item,
                        switchDoToday = switchDoToday,
                        onTapStartTime = onTapStartTime,
                        onTapEndTime = onTapEndTime,
                        switchWillSpeak = switchWillSpeak,
                        onLongClickListItem = onTapDeleteMeeting
                    )
                    if (index != listItems.size - 1) {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
        Row(
            modifier = Modifier.padding(
                end = 15.dp,
                bottom = 10.dp
            )
        ) {
            CreateNewButton(
                onClick = onTapCreateNew
            )
            Spacer(modifier = Modifier.width(5.dp))
            ShareButton(
                onClick = onTapSendButton
            )
        }
    }
}

@Composable
private fun CreateNewButton(
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(MeetingInfoScreenButton)
            .debouncedClickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Create new icon",
            modifier = Modifier.size(30.dp),
            tint = MeetingInfoScreenButtonIcon
        )
    }
}

@Composable
private fun ShareButton(
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(MeetingInfoScreenButton)
            .debouncedClickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = "Share icon",
            modifier = Modifier.size(30.dp),
            tint = MeetingInfoScreenButtonIcon
        )
    }
}


@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun MeetingInfoScreenPreview() {
    MeetingDesuTheme {
        Surface {
            MeetingInfoScreenContent(
                listItems = ImmutableList(
                    value = listOf(
                        MeetingInfoListItemModel(
                            id = 1,
                            doToday = true,
                            startTime = "9:00",
                            endTime = "10:00",
                            willSpeak = true
                        ),
                        MeetingInfoListItemModel(
                            id = 2,
                            doToday = false,
                            startTime = "12:00",
                            endTime = "13:00",
                            willSpeak = false
                        )
                    ),
                ),
                switchDoToday = { _: Int, _: Boolean -> },
                onTapStartTime = {},
                onTapEndTime = {},
                onTapCreateNew = {},
                onTapDeleteMeeting = {},
                switchWillSpeak = { _: Int, _: Boolean -> },
                onTapSendButton = {}
            )
        }
    }
}


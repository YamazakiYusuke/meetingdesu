package com.example.meetingdesu.ui.compose.viewModel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meetingdesu.database.room.entity.Meeting
import com.example.meetingdesu.extension.DateTimeFormatterType
import com.example.meetingdesu.extension.toString
import com.example.meetingdesu.repository.IMeetingInfoRepository
import com.example.meetingdesu.ui.compose.view_parts.MeetingInfoListItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Collections.emptyList

class MeetingInfoViewModel(
    private val repository: IMeetingInfoRepository
) : ViewModel() {
    private val _state: MutableStateFlow<List<Meeting>> = MutableStateFlow(emptyList())
    private val _uiState = MutableStateFlow(MeetingInfoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAll().collect{ meetings ->
                _state.value = meetings
                _uiState.value = _uiState.value.copy(
                    listItems = meetings.map { meeting ->
                        MeetingInfoListItemModel(
                            id = meeting.id,
                            doToday = meeting.doToday,
                            startTime = meeting.startTimeLocalTime().toString(DateTimeFormatterType.HHmm),
                            endTime = meeting.endTimeLocalTime().toString(DateTimeFormatterType.HHmm),
                            willSpeak = meeting.willSpeak,
                        )
                    }
                )
            }
        }
    }

    fun createMeeting(startTime: LocalTime) {
        viewModelScope.launch {
            repository.upsert(
                Meeting.create(
                    doToday = true,
                    startTime = startTime,
                    endTime = startTime.plusHours(1),
                    willSpeak = true,
                )
            )
        }
    }

    fun deleteMeeting(id: Int) {
        val meeting = _state.value.firstOrNull { it.id == id } ?: return
        viewModelScope.launch {
            repository.delete(
                meeting
            )
        }
    }

    fun changeDoToday(id: Int, newDoToday: Boolean) {
        val meeting = _state.value.firstOrNull { it.id == id } ?: return
        viewModelScope.launch {
            repository.upsert(
                meeting.copy(
                    doToday = newDoToday
                )
            )
        }
    }


    fun changeStartTime(id: Int, newStartTime: LocalTime) {
        val meeting = _state.value.firstOrNull { it.id == id } ?: return
        viewModelScope.launch {
            repository.upsert(
                meeting.changeStartTime(newStartTime)
            )
        }
    }

    fun changeEndTime(id: Int, newEndTime: LocalTime) {
        val meeting = _state.value.firstOrNull { it.id == id } ?: return
        viewModelScope.launch {
            repository.upsert(
                meeting.changeEndTime(newEndTime)
            )
        }
    }

    fun changeWillSpeak(id: Int, newWillSpeak: Boolean) {
        val meeting = _state.value.firstOrNull { it.id == id } ?: return
        viewModelScope.launch {
            repository.upsert(
                meeting.copy(
                    willSpeak = newWillSpeak
                )
            )
        }
    }

    fun showTimerPickerDialogToEditStart(id: Int) {
        val meeting = _state.value.firstOrNull { it.id == id } ?: return
        val newEvent = MeetingInfoEvent.ShowTimePickerDialogToEditStart(id, meeting.startTimeLocalTime())
        _uiState.value = _uiState.value.copy(
            events = _uiState.value.events + newEvent
        )
    }

    fun showTimerPickerDialogToEditEnd(id: Int) {
        val meeting = _state.value.firstOrNull { it.id == id } ?: return
        val newEvent = MeetingInfoEvent.ShowTimePickerDialogToEditEnd(id, meeting.endTimeLocalTime())
        _uiState.value = _uiState.value.copy(
            events = _uiState.value.events + newEvent
        )
    }

    fun showTimerPickerDialogToCreateNew() {
        val newEvent = MeetingInfoEvent.ShowTimePickerDialogToCreateNew
        _uiState.value = _uiState.value.copy(
            events = _uiState.value.events + newEvent
        )
    }

    fun showAlertDialogToDeleteMeeting(id: Int) {
        val meeting = _state.value.firstOrNull { it.id == id } ?: return
        val start = meeting.startTimeLocalTime().toString(DateTimeFormatterType.HHmm)
        val end = meeting.endTimeLocalTime().toString(DateTimeFormatterType.HHmm)
        val newEvent = MeetingInfoEvent.ShowAlertDialogToDeleteMeeting(
            id = id,
            meetingName = "$start~$end"
        )
        _uiState.value = _uiState.value.copy(
            events = _uiState.value.events + newEvent
        )
    }

    fun sendMeetingsInfo() {
        val text = _state.value.filter { it.doToday }.map {  meeting ->
            val start = meeting.startTimeLocalTime().toString(DateTimeFormatterType.HHmm)
            val end = meeting.endTimeLocalTime().toString(DateTimeFormatterType.HHmm)
            val speak = if (meeting.willSpeak) {
                "話す"
            } else {
                "話さない"
            }
            "$start~$end($speak)"
        }.joinToString(separator = "\n")
        val newEvent = MeetingInfoEvent.SendMeetingInfo(text)
        _uiState.value = _uiState.value.copy(
            events = _uiState.value.events + newEvent
        )
    }

    fun consumeEvent(event: MeetingInfoEvent) {
        val newEvents = _uiState.value.events.filterNot { it == event }
        _uiState.value = _uiState.value.copy(
            events = newEvents
        )
    }
}

@Immutable
data class MeetingInfoUiState(
    val events: List<MeetingInfoEvent> = emptyList(),
    val listItems: List<MeetingInfoListItemModel> = emptyList(),
)

sealed class MeetingInfoEvent {
    data class Error(val message: String) : MeetingInfoEvent()
    data class ShowTimePickerDialogToEditStart(val id: Int, val initTime: LocalTime) : MeetingInfoEvent()
    data class ShowTimePickerDialogToEditEnd(val id: Int, val initTime: LocalTime) : MeetingInfoEvent()
    data object ShowTimePickerDialogToCreateNew : MeetingInfoEvent()
    data class ShowAlertDialogToDeleteMeeting(val id: Int, val meetingName: String) : MeetingInfoEvent()
    data class SendMeetingInfo(val text: String) : MeetingInfoEvent()
}
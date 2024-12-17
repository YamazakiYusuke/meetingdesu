package com.example.meetingdesu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.meetingdesu.database.AppDatabase
import com.example.meetingdesu.repository.MeetingInfoRepository
import com.example.meetingdesu.ui.compose.screen.MeetingInfoScreen
import com.example.meetingdesu.ui.compose.viewModel.MeetingInfoViewModel
import com.example.meetingdesu.ui.theme.MeetingDesuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeetingDesuTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MeetingInfoScreen(
                        MeetingInfoViewModel(
                            MeetingInfoRepository(
                                AppDatabase.getInstance(this).meetingDao()
                            )
                        )
                    )
                }
            }
        }
    }
}

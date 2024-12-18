package com.example.meetingdesu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.meetingdesu.database.AppDatabase
import com.example.meetingdesu.extension.toSp
import com.example.meetingdesu.repository.MeetingInfoRepository
import com.example.meetingdesu.ui.compose.screen.MeetingInfoScreen
import com.example.meetingdesu.ui.compose.viewModel.MeetingInfoViewModel
import com.example.meetingdesu.ui.theme.MeetingDesuTheme
import com.example.meetingdesu.ui.theme.MeetingInfoScreenHeader
import com.example.meetingdesu.ui.theme.MeetingInfoScreenHeaderText

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
                    content()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun content() {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MeetingInfoScreenHeader,
                        titleContentColor = MeetingInfoScreenHeaderText,
                    ),
                    title = {
                        Row {
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(
                                text = "Meeting",
                                fontSize = 25.dp.toSp(),
                                color = MeetingInfoScreenHeaderText
                            )
                        }
                    },
                )
            }
        ) { innerPadding ->
            MeetingInfoScreen(
                MeetingInfoViewModel(
                    MeetingInfoRepository(
                        AppDatabase.getInstance(this).meetingDao()
                    )
                ),
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

package com.alexvolkov.dazntestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexvolkov.dazntestapp.presentation.navigation.EventsScreen
import com.alexvolkov.dazntestapp.presentation.navigation.ScheduleScreen
import com.alexvolkov.dazntestapp.presentation.navigation.VideoScreen
import com.alexvolkov.dazntestapp.presentation.viemodel.EventsViewModel
import com.alexvolkov.dazntestapp.presentation.view.BottomNavigationBar
import com.alexvolkov.dazntestapp.presentation.view.EventsList
import com.alexvolkov.dazntestapp.ui.theme.DaznTestAppTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            DaznTestAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding()) {
                        val eventsListEventsViewModel = koinViewModel<EventsViewModel>()
                        NavHost(navController = navController, startDestination = EventsScreen) {
                            composable<EventsScreen> {
                                EventsList(
                                    innerPaddings = innerPadding,
                                    eventsViewModel = eventsListEventsViewModel
                                )
                            }
                            composable<ScheduleScreen> {
                                Text("Schedule")
                            }
                            composable<VideoScreen> {
                                Text("Video")
                            }
                        }
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            BottomNavigationBar(
                                modifier = Modifier.height(80.dp),
                                navController = navController
                            )
                        }

                    }
                }
            }
        }
    }
}

package com.alexvolkov.dazntestapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.alexvolkov.dazntestapp.presentation.navigation.EventsScreen
import com.alexvolkov.dazntestapp.presentation.navigation.ScheduleScreen
import com.alexvolkov.dazntestapp.presentation.navigation.VideoScreen
import com.alexvolkov.dazntestapp.presentation.viemodel.EventsViewModel
import com.alexvolkov.dazntestapp.presentation.viemodel.ScheduleViewModel
import com.alexvolkov.dazntestapp.presentation.view.BottomNavigationBar
import com.alexvolkov.dazntestapp.presentation.view.EventsList
import com.alexvolkov.dazntestapp.presentation.view.ScheduleList
import com.alexvolkov.dazntestapp.presentation.view.VideoPlayer
import com.alexvolkov.dazntestapp.service.MediaPlayerService
import com.alexvolkov.dazntestapp.ui.theme.DaznTestAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute by remember {
                derivedStateOf {
                    currentBackStackEntry?.destination?.route ?: "Home"
                }
            }

            DaznTestAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        AnimatedVisibility(
                            visible = currentRoute.contains(VideoScreen::class.qualifiedName.toString())
                                .not(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            BottomNavigationBar(
                                navController = navController,
                                modifier = Modifier.height(80.dp)
                            )
                        }
                    }) { innerPadding ->
                    Box(modifier = Modifier.padding()) {
                        val eventsListEventsViewModel = koinViewModel<EventsViewModel>()
                        val scheduleViewModel = koinViewModel<ScheduleViewModel>()
                        NavHost(navController = navController, startDestination = EventsScreen) {
                            composable<EventsScreen> {
                                EventsList(
                                    innerPaddings = innerPadding,
                                    eventsViewModel = eventsListEventsViewModel
                                ) {
                                    navController.navigate(VideoScreen(it))
                                }
                            }
                            composable<ScheduleScreen> {
                                ScheduleList(
                                    innerPaddings = innerPadding,
                                    scheduleViewModel = scheduleViewModel
                                )
                            }
                            composable<VideoScreen> { backStackEntry ->
                                val video: VideoScreen = backStackEntry.toRoute()
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black),
                                    contentAlignment = Alignment.Center
                                ) {
                                    VideoPlayer(
                                        videoUrl = video.videoUrl,
                                        navController = navController
                                    ) {
                                        startAndBindService()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startAndBindService() {
        Intent(this, MediaPlayerService::class.java).also { intent ->
            startForegroundService(intent)
        }
    }
}

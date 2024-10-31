package com.alexvolkov.dazntestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexvolkov.dazntestapp.presentation.navigation.EventsScreen
import com.alexvolkov.dazntestapp.presentation.navigation.ScheduleScreen
import com.alexvolkov.dazntestapp.presentation.navigation.VideoScreen
import com.alexvolkov.dazntestapp.presentation.view.BottomNavigationBar
import com.alexvolkov.dazntestapp.ui.theme.DaznTestAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            DaznTestAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        NavHost(navController = navController, startDestination = EventsScreen) {
                            composable<EventsScreen> { innerPadding ->
                                Text("Events")
                            }
                            composable<ScheduleScreen> {
                                Text("Schedule")
                            }
                            composable<VideoScreen> {
                                Text("Video")
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        BottomNavigationBar(
                            modifier = Modifier
                                .height(60.dp),
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

package com.alexvolkov.dazntestapp.presentation.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.alexvolkov.dazntestapp.R
import com.alexvolkov.dazntestapp.presentation.navigation.EventsScreen
import com.alexvolkov.dazntestapp.presentation.navigation.Route
import com.alexvolkov.dazntestapp.presentation.navigation.ScheduleScreen

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    BottomNavigation(modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimary) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        BottomNavItem.values.forEach { item ->
            BottomNavigationItem(
                selected = currentDestination?.route == item.route::class.qualifiedName,
                onClick = {
                    if(currentDestination?.route != item.route::class.qualifiedName) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    Icon(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 25.dp),
                        painter = painterResource(id = item.iconRes),
                        contentDescription = null
                    )
                },
                label = { Text(text = item.label) }
            )
        }
    }
}

sealed class BottomNavItem(val route: Route, val iconRes: Int, val label: String) {
    companion object {
        val values = listOf(Events, Schedule)
    }

    data object Events : BottomNavItem(EventsScreen, R.drawable.events_icon, "Events")
    data object Schedule : BottomNavItem(ScheduleScreen, R.drawable.schedule_icon, "Schedule")
}
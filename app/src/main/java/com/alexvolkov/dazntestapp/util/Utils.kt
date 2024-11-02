package com.alexvolkov.dazntestapp.util

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.alexvolkov.dazntestapp.receivers.NetworkStateReceiver
import com.alexvolkov.dazntestapp.util.Utils.isNetworkAvailable
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

object Utils {
    fun parseDate(dateString: String): Date {
        val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
        val zonedDateTime = ZonedDateTime.parse(dateString, formatter)
        return Date.from(zonedDateTime.toInstant())
    }

    fun formatDate(date: Date): String {
        val now = LocalDate.now()
        val targetDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val targetDateTime = date.toInstant().atZone(ZoneId.systemDefault())
        val daysBetween = java.time.temporal.ChronoUnit.DAYS.between(now, targetDate)

        return when (daysBetween) {
            -1L -> "Yesterday, ${DateTimeFormatter.ofPattern("HH:mm").format(targetDateTime)}"
            0L -> "Today, ${DateTimeFormatter.ofPattern("HH:mm").format(targetDateTime)}"
            1L -> "Tomorrow, ${DateTimeFormatter.ofPattern("HH:mm").format(targetDateTime)}"
            2L -> "In two days"
            3L -> "In three days"
            else -> DateTimeFormatter.ofPattern("dd.MM.yyyy").format(targetDate)
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}

@Composable
fun CheckInternetConnection(
    onStateChanged: (Boolean) -> Unit
) {
    val context = LocalContext.current
    var isConnected by remember { mutableStateOf(isNetworkAvailable(context)) }

    DisposableEffect(Unit) {
        val receiver = NetworkStateReceiver { isConnected = it }
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(receiver, filter)

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    LaunchedEffect(isConnected) {
        onStateChanged(isConnected)
    }
}
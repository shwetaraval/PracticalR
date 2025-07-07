package com.example.myapplication.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.myapplication.ui.schedule.ScheduleScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ This makes status bar visible and content NOT overlap it
        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            MyApplicationTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentRoute by navController.currentBackStackEntryAsState()

    val tabs = listOf("schedule" to "Schedules", "games" to "Games")
    val selectedIndex = tabs.indexOfFirst { it.first == currentRoute?.destination?.route }.coerceAtLeast(0)

    Scaffold(
        topBar = {
            TabRow(selectedTabIndex = selectedIndex) {
                tabs.forEachIndexed { index, (route, title) ->
                    Tab(
                        selected = index == selectedIndex,
                        onClick = {
                            if (currentRoute?.destination?.route != route) {
                                navController.navigate(route) {
                                    popUpTo("schedule") { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        },
                        text = { Text(title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "schedule",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("schedule") { ScheduleScreen() }
            composable("games") { GamesScreen() }
        }
    }
}

@Composable
fun GamesScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Games screen (To be implemented)", style = MaterialTheme.typography.titleLarge)
    }
}

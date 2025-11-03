package com.example.bdeorga.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bdeorga.activity.screen.EvenementScreen
import com.example.bdeorga.screens.EventDetailScreen
import com.example.bdeorga.activity.screen.TacheScreen
import com.example.bdeorga.ui.theme.MyBdeOrgaTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyBdeOrgaTheme {
                HomeNav()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeNav() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            BottomBar(navController, currentRoute)
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "evenements",
        ) {
            composable("taches") { TacheScreen(navController) }
            composable("evenements") { EvenementScreen(navController) }
            composable("profil") { ProfileScreen() }

            composable("eventDetail/{eventId}") { backStackEntry ->
                val eventId = backStackEntry.arguments?.getString("eventId")?.toIntOrNull()
                EventDetailScreen(navController, eventId)
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController, currentRoute: String?) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
        val items = listOf(
            BottomItem("taches", Icons.AutoMirrored.Filled.List, "Tâches"),
            BottomItem("evenements", Icons.Default.Event, "Événements")

        )

        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { navController.navigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (selected) Color.Black else Color.Gray
                    )
                },
                label = {
                    Text(item.label, color = if (selected) Color.Black else Color.Gray)
                }
            )
        }
    }
}

data class BottomItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)


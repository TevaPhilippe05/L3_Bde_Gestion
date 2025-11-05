package com.example.bdeorga.activity.screen

import ProfileStorage
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bdeorga.activity.MainActivity
import com.example.bdeorga.activity.functions.tronquerText
import com.example.bdeorga.model.Evenement
import com.example.bdeorga.request.EventRequest
import td.info507.bdeorga.storage.UserStorage
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.widget.Toast
import com.example.bdeorga.notifications.NotificationReceiver
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import android.provider.Settings
import androidx.compose.foundation.layout.PaddingValues
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvenementScreen(navController: NavHostController) {
    val context = LocalContext.current
    val user = remember { UserStorage.get() }
    val events = remember { mutableStateListOf<Evenement>() }
    var expanded by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Récupération persistante
    LaunchedEffect(Unit) {
        val saved = ProfileStorage.getUri(context, user?.email)
        imageUri = saved?.toUri() ?: user?.profileImageUri?.toUri()

        EventRequest(context) { fetchedEvents ->
            events.clear()
            events.addAll(fetchedEvents)
            scheduleNotifications(context, fetchedEvents)
        }.fetchEvents()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Évenements",
                        fontWeight = FontWeight.Bold
                    ) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                actions = {
                    Box {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { expanded = true } // Ouvre le menu au clic
                                .padding(horizontal = 8.dp) // Ajoute un peu d'espace
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.onSurfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                if (imageUri != null) {
                                    Image(
                                        painter = rememberAsyncImagePainter(imageUri),
                                        contentDescription = "Photo de profil",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    val initial = user?.email?.firstOrNull()?.uppercase() ?: "?"
                                    Text(initial, color = Color.White, style = MaterialTheme.typography.titleMedium)
                                }
                            }
                            Text(
                                text = user?.prenom ?: "",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface // Couleur du texte du thème
                            )
                        }

                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            DropdownMenuItem(
                                text = { Text("Profil") },
                                onClick = {
                                    expanded = false
                                    navController.navigate("profil")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Déconnexion", color = Color.Red) },
                                onClick = {
                                    expanded = false
                                    UserStorage.clear(context)
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    context.startActivity(intent)
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(8.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(events.size) { i ->
                val ev = events[i]

                Card(
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("eventDetail/${ev.id}")
                        },
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(ev.titre,
                            fontSize = TextUnit(
                                MaterialTheme.typography.bodyMedium.fontSize.value + 6,
                                TextUnitType.Sp)
                        )
                        Text(
                            tronquerText(ev.description, 50),
                            fontSize = TextUnit(
                                MaterialTheme.typography.bodyMedium.fontSize.value - 2,
                                TextUnitType.Sp
                            )
                        )
                        Text("${ev.date} à ${ev.heure}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(ev.lieu, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

private fun scheduleNotifications(context: Context, events: List<Evenement>) {

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (!alarmManager.canScheduleExactAlarms()) {
            Toast.makeText(context, "Permission d'alarme requise pour les rappels", Toast.LENGTH_LONG).show()

            Intent(
                Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                "package:${context.packageName}".toUri()
            ).also {
                // Il faut démarrer cette activité depuis le contexte
                context.startActivity(it)
            }
            return
        }
    }

    val now = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    events.forEach { event ->
        try {
            val eventDateTime = LocalDateTime.parse("${event.date} ${event.heure}", formatter)
            val reminderDateTime = eventDateTime.minusDays(1) // Rappel 1 jour avant

            val intent = Intent(context, NotificationReceiver::class.java).apply {
                putExtra(NotificationReceiver.EVENT_TITLE_KEY, event.titre)
                putExtra(NotificationReceiver.NOTIFICATION_ID_KEY, event.id)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                event.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            if (reminderDateTime.isAfter(now)) {
                // si e rappel est dans le futur
                val triggerAtMillis = reminderDateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )

            } else if (eventDateTime.isAfter(now)) {
                // si le rappel est passé, mais l'événement est à venir
                val triggerAtMillis = System.currentTimeMillis() + 1000 // 1s
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
            // si l'événement est passé (on ne fait rien)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


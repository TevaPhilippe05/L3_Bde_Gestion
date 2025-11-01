package com.example.bdeorga.screens

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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
        imageUri = saved?.let { Uri.parse(it) } ?: user?.profileImageUri?.let { Uri.parse(it) }

        EventRequest(context) {
            events.clear()
            events.addAll(it)
        }.fetchEvents()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Événements") },
                actions = {
                    Box {
                        IconButton(onClick = { expanded = true }) {
                            if (imageUri != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(imageUri),
                                    contentDescription = "Profil",
                                    modifier = Modifier.size(36.dp).clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color.LightGray),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val initial = user?.email?.firstOrNull()?.uppercase() ?: "?"
                                    Text(initial, color = Color.White)
                                }
                            }
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
                .padding(8.dp)
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
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF381719))
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
                        Text("${ev.date} à ${ev.heure}", color = Color.Gray)
                        Text(ev.lieu, color = Color.Gray)
                    }
                }
            }
        }
    }
}


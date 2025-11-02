package com.example.bdeorga.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bdeorga.model.Evenement
import com.example.bdeorga.model.User
import com.example.bdeorga.request.EventRequest
import com.example.bdeorga.request.MembersRequest
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(navController: NavHostController, eventId: Int?){
    val context = LocalContext.current
    var event by remember { mutableStateOf<Evenement?>(null) }

    LaunchedEffect(eventId) {
        if (eventId != null) {
            EventRequest(context) { events ->
                event = events.find { it.id == eventId }
            }.fetchEvents()
        }
    }

    if (event == null) {
        Text("Chargement ou événement introuvable...")
        return
    }

    val ev = event!!

    val membreIds = ev.membresAttribues
    var membres by remember { mutableStateOf(listOf<User>()) }

    LaunchedEffect(ev) {
        MembersRequest(context).getMembersByIds(membreIds) {
            membres = it
        }
    }

    Column(
        modifier = Modifier
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)

    ) {
        Text(ev.titre,
            fontSize = TextUnit(
                MaterialTheme.typography.bodyMedium.fontSize.value + 18,
                TextUnitType.Sp),
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Text("Lieu : " + ev.lieu,
            fontSize = TextUnit(
                MaterialTheme.typography.bodyMedium.fontSize.value + 8,
                TextUnitType.Sp))

        Text("Date : ${ev.date} à ${ev.heure}",
            fontSize = TextUnit(
                MaterialTheme.typography.bodyMedium.fontSize.value + 8,
                TextUnitType.Sp))

        Text("Budjet : " + ev.budget)

        Column {
            Text("Membres assignés à cette tâche :")
            membres.forEach { user ->
                Text("${user.prenom} ${user.nom}")
            }
        }

        Text("Nombre de participants prévus : " + ev.nombreParticipantsPrevus)

        Text("Catégorie : " + ev.categorie)

        Text(ev.description
        )
    }
}
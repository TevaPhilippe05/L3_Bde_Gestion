package com.example.bdeorga.request

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bdeorga.model.Evenement
import org.json.JSONObject

class EventRequest(private val context: Context, private val onComplete: (List<Evenement>) -> Unit) {
    fun fetchEvents() {
        val queue = Volley.newRequestQueue(context)
        val url = "http://51.68.91.213/gr-1-3/bde.json"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val events = parseEvents(response)
                onComplete(events)
                Toast.makeText(context, "Événements chargés", Toast.LENGTH_SHORT).show()
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(context, "Erreur lors du chargement", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(request)
    }

    private fun parseEvents(json: JSONObject): List<Evenement> {
        val events = mutableListOf<Evenement>()
        val jsonEvents = json.getJSONArray("evenements")
        for (i in 0 until jsonEvents.length()) {
            val ev = jsonEvents.getJSONObject(i)
            val membres = ev.getJSONArray("membres_attribues")
            val membresList = mutableListOf<Int>()
            for (j in 0 until membres.length()) {
                membresList.add(membres.getInt(j))
            }
            events.add(
                Evenement(
                    id = ev.getInt("id"),
                    titre = ev.getString("titre"),
                    description = ev.getString("description"),
                    date = ev.getString("date"),
                    heure = ev.getString("heure"),
                    lieu = ev.getString("lieu"),
                    statut = ev.getString("statut"),
                    membresAttribues = membresList,
                    budget = ev.getDouble("budget"),
                    nombreParticipantsPrevus = ev.getInt("nombre_participants_prevus"),
                    categorie = ev.getString("categorie")
                )
            )
        }
        return events
    }
}

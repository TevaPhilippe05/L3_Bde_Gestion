package com.example.bdeorga.request

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import com.example.bdeorga.model.User

class MembersRequest(private val context: Context) {

    fun getMembersByIds(memberIds: List<Int>, onComplete: (List<User>) -> Unit) {
        val queue = Volley.newRequestQueue(context)
        val url = "http://51.68.91.213/gr-1-3/bde.json"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val membres = response.getJSONArray("membres_bureau")
                val users = mutableListOf<User>()

                for (i in 0 until membres.length()) {
                    val membre = membres.getJSONObject(i)
                    val id = membre.getInt("id")
                    if (memberIds.contains(id)) {
                        users.add(
                            User(
                                id = id,
                                nom = membre.getString("nom"),
                                prenom = membre.getString("prenom"),
                                role = membre.getString("role"),
                                email = membre.getString("email"),
                                telephone = membre.getString("telephone")
                            )
                        )
                    }
                }
                onComplete(users)
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(context, "Erreur réseau", Toast.LENGTH_SHORT).show()
                onComplete(emptyList())
            }
        )

        queue.add(request)
    }
}

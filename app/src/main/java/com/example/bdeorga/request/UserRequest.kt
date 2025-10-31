package com.example.bdeorga.request

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import com.example.bdeorga.model.User
import td.info507.bdeorga.storage.UserStorage

class UserRequest(private val context: Context, private val onComplete: (Boolean, User?) -> Unit) {

    fun login(email: String, password: String) {
        val queue = Volley.newRequestQueue(context)
        val url = "http://51.68.91.213/gr-1-3/bde.json"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val user = checkCredentials(response, email, password)
                if (user != null) {
                    onComplete(true, user)
                    Toast.makeText(context, "Connexion réussie", Toast.LENGTH_SHORT).show()
                } else {
                    onComplete(false, null)
                    Toast.makeText(context, "Identifiants incorrects", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                error.printStackTrace()
                onComplete(false, null)
                Toast.makeText(context, "Erreur réseau", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }

    private fun checkCredentials(json: JSONObject, email: String, password: String): User? {
        val membres = json.getJSONArray("membres_bureau")

        for (i in 0 until membres.length()) {
            val membre = membres.getJSONObject(i)
            if (
                membre.getString("email") == email &&
                membre.getString("motdepasse") == password
            ) {
                return User(
                    id = membre.getInt("id"),
                    nom = membre.getString("nom"),
                    prenom = membre.getString("prenom"),
                    role = membre.getString("role"),
                    email = membre.getString("email"),
                    telephone = membre.getString("telephone")
                )
            }
        }
        return null
    }
}

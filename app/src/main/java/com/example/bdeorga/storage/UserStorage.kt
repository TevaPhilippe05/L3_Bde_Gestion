package td.info507.bdeorga.storage

import android.content.Context
import com.example.bdeorga.model.User

object UserStorage {
    private var currentUser: User? = null
    private const val PREF_NAME = "user_prefs"
    private const val KEY_ID = "id"
    private const val KEY_NOM = "nom"
    private const val KEY_PRENOM = "prenom"
    private const val KEY_ROLE = "role"
    private const val KEY_EMAIL = "email"
    private const val KEY_TEL = "telephone"

    fun save(context: Context, user: User) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putInt(KEY_ID, user.id)
            putString(KEY_NOM, user.nom)
            putString(KEY_PRENOM, user.prenom)
            putString(KEY_ROLE, user.role)
            putString(KEY_EMAIL, user.email)
            putString(KEY_TEL, user.telephone)
            apply()
        }
        currentUser = user
    }

    fun load(context: Context): User? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val id = prefs.getInt(KEY_ID, -1)
        if (id == -1) return null

        val user = User(
            id = id,
            nom = prefs.getString(KEY_NOM, "") ?: "",
            prenom = prefs.getString(KEY_PRENOM, "") ?: "",
            role = prefs.getString(KEY_ROLE, "") ?: "",
            email = prefs.getString(KEY_EMAIL, "") ?: "",
            telephone = prefs.getString(KEY_TEL, "") ?: ""
        )
        currentUser = user
        return user
    }

    fun clear(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        currentUser = null
    }

    fun get(): User? = currentUser
}

import android.content.Context
import android.net.Uri
import td.info507.bdeorga.storage.UserStorage
import java.io.File

object ProfileStorage {

    private fun fileForUser(context: Context, email: String?): File {
        val safeEmail = email?.replace("[^A-Za-z0-9_]".toRegex(), "_") ?: "unknown"
        return File(context.filesDir, "profile_image_$safeEmail.txt")
    }

    fun saveUri(context: Context, email: String?, uri: String) {
        val file = fileForUser(context, email)
        file.writeText(uri)
    }

    fun getUri(context: Context, email: String?): String? {
        val file = fileForUser(context, email)
        return if (file.exists()) file.readText() else null
    }

    fun clear(context: Context, email: String?) {
        val file = fileForUser(context, email)
        if (file.exists()) file.delete()
    }

    fun clearAll(context: Context) {
        context.filesDir.listFiles()?.forEach {
            if (it.name.startsWith("profile_image_")) it.delete()
        }
    }
}

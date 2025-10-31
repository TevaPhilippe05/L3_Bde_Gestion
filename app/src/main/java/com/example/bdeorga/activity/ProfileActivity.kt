package com.example.bdeorga.screens

import ProfileStorage
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.bdeorga.activity.MainActivity
import td.info507.bdeorga.storage.UserStorage
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: androidx.navigation.NavHostController) {
    val context = LocalContext.current
    val user = UserStorage.get()
    var imageUri by remember {
        mutableStateOf(ProfileStorage.getUri(context, user?.email)?.let { Uri.parse(it) })
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val localUri = saveUriToLocalFile(context, it)
            imageUri = localUri
            user?.profileImageUri = localUri.toString()
            UserStorage.save(context, user!!)
            ProfileStorage.saveUri(context, user.email, localUri.toString())
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            val uri = saveBitmapToFile(context, it)
            imageUri = uri
            user?.profileImageUri = uri.toString()
            UserStorage.save(context, user!!)
            ProfileStorage.saveUri(context, user.email, uri.toString())
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mon profil") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable { galleryLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Photo de profil",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    val initial = user?.email?.firstOrNull()?.uppercase() ?: "?"
                    Text(initial, color = Color.White, style = MaterialTheme.typography.headlineLarge)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("${user?.prenom} ${user?.nom}", style = MaterialTheme.typography.titleMedium)
            Text(user?.email ?: "", color = Color.Gray)

            Spacer(modifier = Modifier.height(32.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = { galleryLauncher.launch("image/*") }) { Text("Galerie") }
                Button(onClick = { cameraLauncher.launch() }) { Text("Appareil") }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    UserStorage.clear(context)
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Déconnexion", color = Color.White)
            }
        }
    }
}

private fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri {
    val file = File(context.filesDir, "profile_${UUID.randomUUID()}.jpg")
    FileOutputStream(file).use { out -> bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out) }
    return Uri.fromFile(file)
}

private fun saveUriToLocalFile(context: Context, uri: Uri): Uri {
    val file = File(context.filesDir, "profile_${UUID.randomUUID()}.jpg")
    context.contentResolver.openInputStream(uri)?.use { input ->
        FileOutputStream(file).use { output -> input.copyTo(output) }
    }
    return Uri.fromFile(file)
}

package com.example.bdeorga

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bdeorga.ui.theme.MyBdeOrgaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyBdeOrgaTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current

    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var pseudo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center) // tout centré au milieu
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            UserImage(imageBitmap)

            // Boutons photo
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val buttonModifier = Modifier
                    .weight(1f)
                    .background(color = Color(255, 187, 51), shape = CircleShape)

                CameraButton(buttonModifier) { bitmap -> imageBitmap = bitmap }
                GalleryButton(buttonModifier) { bitmap -> imageBitmap = bitmap }
            }

            // Champ pseudo
            TextField(
                value = pseudo,
                onValueChange = { pseudo = it },
                singleLine = true,
                label = { Text("Pseudo") },
                modifier = Modifier.fillMaxWidth()
            )

            // Champ password
            TextField(
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                label = { Text("Mot de passe") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (pseudo.isNotBlank() and password.isNotBlank()) {
                        val intent = Intent(context, ProfileActivity::class.java)
                        context.startActivity(intent)
                    } else if (pseudo.isBlank() and password.isBlank()) {
                        Toast.makeText(context, "Veuillez entrer un pseudo et un mot de passe", Toast.LENGTH_SHORT).show()
                    }  else if (pseudo.isBlank()) {
                        Toast.makeText(context, "Veuillez entrer un pseudo", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Veuillez entrer un mot de passe", Toast.LENGTH_SHORT).show()
                    }
                },

                shape = CircleShape,

                ) {
                Text("Connexion")
            }
        }


        // Bouton en bas
        Button(
            onClick = {},
            enabled = false,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Gray,
                disabledContainerColor = Color.Gray
            )
        ) {
            Text(
                text = "bdeOrga",
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MyBdeOrgaTheme {
        MainScreen()
    }
}

package com.example.bdeorga

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bdeorga.ui.theme.MyBdeOrgaTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            MyBdeOrgaTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(){

    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    val context = LocalContext.current
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted){
            Toast.makeText(context, "Permission granted !!!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission not granted ...", Toast.LENGTH_SHORT).show()
        }
    }

    var pseudo by remember { mutableStateOf("") }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(WindowInsets.safeDrawing.asPaddingValues())
    ){
        Row(modifier=Modifier
            .align(Alignment.TopCenter)
        ) {
            Column(
                modifier = Modifier
                    .weight(0.25f)
                    .height(100.dp)
            ) {
                UserImage(imageBitmap)
                Image(
                    painter = painterResource(R.drawable.ic_user),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentDescription = "Image du profil"
                )
            }
            Column(modifier = Modifier.weight(0.75f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Pseudo : ",
                        modifier = Modifier.padding(8.dp)
                    )
                    TextField(
                        value = pseudo,
                        onValueChange = { pseudo = it },
                        singleLine = true
                    )
                }
                val modifier = Modifier
                    .weight(1f)
                    .background(color = Color(255, 187, 51), shape = CircleShape)
                Row(modifier = Modifier.fillMaxWidth()) {
                    CameraButton(modifier) { bitmap -> imageBitmap = bitmap }

                    // Ici on utilise ton vrai bouton galerie
                    GalleryButton(modifier) { bitmap -> imageBitmap = bitmap }
                }
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            Button(
                onClick = {
                    //                    Toast.makeText(context, "coucou", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, ProfileActivity::class.java)
                    context.startActivity(intent)
                },
                shape = CircleShape
            ) {
                Text(text = "" + context.resources.getText(R.string.app_name), color = Color.White)
            }
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
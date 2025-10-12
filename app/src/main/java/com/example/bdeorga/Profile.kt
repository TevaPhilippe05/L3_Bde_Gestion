package com.example.bdeorga

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun UserImage(imageBitmap: ImageBitmap?) {
    val contentDescription = "Photo de profil"
    val modifier = Modifier
        .size(120.dp)
        .background(Color.LightGray, CircleShape)
        .padding(16.dp)
    if (imageBitmap == null) {
        Image(
            painter = painterResource(R.drawable.ic_user),
            contentDescription = contentDescription,
            modifier = modifier
        )
    } else {
        Image(
            bitmap = imageBitmap,
            modifier = modifier,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun CameraButton(modifier : Modifier, callback:(bitmap: ImageBitmap?)-> Unit){
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) {bitmap -> bitmap?.let {callback(it.asImageBitmap())}}

    IconButton(onClick = { cameraLauncher.launch() }, modifier = modifier) {
        Icon(
            painter = painterResource(R.drawable.ic_photo),
            contentDescription = "Ouvre l'appareil photo",
            tint = Color.White
        )
    }
}

@Composable
fun GalleryButton(modifier: Modifier, callback: (bitmap: ImageBitmap?) -> Unit) {
    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val bitmap = context.contentResolver.openInputStream(it)?.use { stream ->
                android.graphics.BitmapFactory.decodeStream(stream)
            }
            bitmap?.let { callback(it.asImageBitmap()) }
        }
    }

    IconButton(onClick = { galleryLauncher.launch("image/*") }, modifier = modifier) {
        Icon(
            painter = painterResource(R.drawable.ic_gallerie),
            contentDescription = "Ouvre la galerie",
            tint = Color.White
        )
    }
}
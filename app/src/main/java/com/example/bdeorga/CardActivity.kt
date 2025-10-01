package com.example.bdeorga

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.bdeorga.ui.theme.MyBdeOrgaTheme


class CardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyBdeOrgaTheme {
                CardScreen()
            }
        }
    }
}

@Composable
fun CardScreen() {
    Box(Modifier.fillMaxWidth().fillMaxHeight().background(color = Color(68, 170, 68))){
        Column (
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)
                .align(Alignment.TopEnd) // alignment of the box
        ){
            Image(
                painter = painterResource(R.drawable.ic_user),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .weight(0.75f),
                contentDescription = "Image du profil"
            )
            Text(
                text = "Toto",
                modifier = Modifier
                    .weight(0.25f)
                    .fillMaxSize(),
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .height(100.dp)
                .fillMaxWidth()
        ){
            Text(
                text = "3",
                modifier = Modifier
                    .weight(0.75f)
                    .fillMaxSize(),
                textAlign = TextAlign.Center,
                fontSize = 15.em,
                color = Color(73, 69, 79)
            )
            Text(
                text="Allez ça passe...",
                modifier = Modifier
                    .weight(0.25f)
                    .fillMaxSize(),
                textAlign = TextAlign.Center,
                color = Color(73, 69, 79)
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyBdeOrgaTheme {
        CardScreen()
    }
}
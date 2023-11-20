package com.codeshinobi.bajeti

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codeshinobi.bajeti.ui.theme.BajetiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BajetiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column() {
                        WelcomeCard()
                        MainOptions()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
@Composable
fun MainOptions() {
    Column {
        MainOptionsCard(text = "Food", modifier = Modifier.fillMaxWidth())
        MainOptionsCard(text = "Transportation")
        MainOptionsCard(text = "Utilities")
    }
}
@Composable
fun MainOptionsCard(text: String,
                    modifier: Modifier = Modifier.fillMaxWidth())
{
    Card(
        shape =MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = modifier.padding(10.dp),
    ) {
        Text(text = text)
    }
}
@Composable
fun WelcomeCard(){
    Card(
        shape =MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)

    ) {
        Column() {
            Greeting("Darlingson")
            Text(text = "Welcome")

        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BajetiTheme {
//        Greeting("Darlingson")
        MainOptions()
    }
}
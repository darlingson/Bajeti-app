package com.codeshinobi.bajeti

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.codeshinobi.bajeti.activities.ExpensesActivity
import com.codeshinobi.bajeti.activities.MainOption
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
                    val context = LocalContext.current
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
        MainOptionsCard(text = "Transportation", modifier = Modifier.fillMaxWidth())
        MainOptionsCard(text = "Utilities", modifier = Modifier.fillMaxWidth())
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainOptionsCard(text: String,
                    modifier: Modifier = Modifier.fillMaxWidth()
)
{
    val context = LocalContext.current
    Card(
        shape =MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = modifier.padding(10.dp).height(80.dp),
        onClick = { Log.d("Click", "CardExample: Card Click")
//            context.startActivity(Intent(context, MainOption::class.java))
            context.startActivity(Intent(context, ExpensesActivity::class.java))
            },
        enabled = true
    ) {
        Text(text = text,
            modifier = Modifier
            .padding(16.dp),
            textAlign = TextAlign.Center,)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeCard(){
    val context = LocalContext.current
    Card(
        shape =MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        onClick = { Log.d("Click", "CardExample: Card Click")
            context.startActivity(Intent(context, ExpensesActivity::class.java))},
        enabled = true
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
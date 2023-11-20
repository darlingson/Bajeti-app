package com.codeshinobi.bajeti.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codeshinobi.bajeti.activities.ui.theme.BajetiTheme

class MainOption : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BajetiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView()
                }
            }
        }
    }
}
@Composable
fun MainView(){
    Column() {
        Greeting2("Android")
        AmountsSection()
    }
}
@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier.height(250.dp)) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        Text(text = "Total Amount reserved for Food : K 80,000")
    }
}
@Composable
fun AmountsSection(){
    Column(
        Modifier.fillMaxWidth()
            .verticalScroll(rememberScrollState())
//        Modifier.fillMaxWidth().border(width = 2.dp, color = Color(255), shape = Shape)
    ) {
        AmountsCard(text = "Bread", Modifier.fillMaxWidth())
        AmountsCard(text = "Sugar", Modifier.fillMaxWidth())
        AmountsCard(text = "Noodles", Modifier.fillMaxWidth())
        AmountsCard(text = "Meat", Modifier.fillMaxWidth())
        AmountsCard(text = "Snacks", Modifier.fillMaxWidth())
        AmountsCard(text = "Oil", Modifier.fillMaxWidth())
        AmountsCard(text = "Flour", Modifier.fillMaxWidth())
        AmountsCard(text = "Kitchen Essentials", Modifier.fillMaxWidth())
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmountsCard(text:String,modifier: Modifier){
    Card(
        shape =MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier
            .padding(10.dp)
            .height(80.dp)
            .fillMaxWidth(),
    ) {
        Text(text = text,
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,)
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    BajetiTheme {
        MainView()
    }
}
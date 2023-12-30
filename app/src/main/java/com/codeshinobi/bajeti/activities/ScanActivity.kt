package com.codeshinobi.bajeti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.codeshinobi.bajeti.ui.theme.BajetiTheme

@OptIn(ExperimentalComposeUiApi::class)
class ScanActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScanMainContent()
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScanMainContent() {
    Column(
        Modifier.fillMaxWidth().fillMaxHeight(),
    ) {
        Column(
            Modifier.fillMaxWidth().fillMaxHeight(0.8f),
        ) {
            Text("Top Column")
        }
        Column(
            Modifier.fillMaxWidth().fillMaxHeight(0.2f),
        ) {
            Text("Bottom Column")
        }
    }
}
@Composable
fun MyComposable() {
    Box(modifier = Modifier.fillMaxSize()) {
        CoilImage(
            data = "https://example.com/image.jpg",
            contentDescription = "Description of the image"
        )
    }
}
@Preview(showBackground = true)
@Composable
fun ScanPreview() {
    BajetiTheme {
//        Greeting("Darlingson")
        ScanMainContent()
    }
}
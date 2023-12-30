package com.codeshinobi.bajeti.activities

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.codeshinobi.bajeti.activities.ui.theme.BajetiTheme
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


class ScanActivity : ComponentActivity() {
    private var mSelectedImage: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BajetiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting8("Android")
                }
            }
        }
    }
    private val pickImage =
        this.registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                mSelectedImage = uri
                runTextRecognition()
            }
        }
    private fun runTextRecognition() {
        mSelectedImage?.let { imageUri ->
            // Use the imageUri to create an InputImage
            val image = InputImage.fromFilePath(this, imageUri)

            // Initialize the TextRecognizer
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            // Process the image
            recognizer.process(image)
                .addOnSuccessListener { texts ->
                    // Handle success
                    processTextRecognitionResult(texts)
                }
                .addOnFailureListener { e ->
                    // Handle failure
                    showToast("Text recognition failed. Please try again.")
                    e.printStackTrace()
                }
        } ?: showToast("No image selected")
    }
    private fun processTextRecognitionResult(texts: Text) {
        val blocks: List<Text.TextBlock> = texts.getTextBlocks()
        if (blocks.size == 0) {
            showToast("No text found")
            return
        }
        mGraphicOverlay.clear()
        for (i in blocks.indices) {
            val lines: List<Text.Line> = blocks[i].getLines()
            for (j in lines.indices) {
                val elements: List<Text.Element> = lines[j].getElements()
                for (k in elements.indices) {
                    val textGraphic: Graphic = TextGraphic(mGraphicOverlay, elements[k])
                    mGraphicOverlay.add(textGraphic)
                }
            }
        }
    }

    private fun showToast(s: String) {
        Toast.makeText(
            this,
            "Text recognition failed for the following reason: $s",
            Toast.LENGTH_LONG,
        ).show()
    }
}

@Composable
fun Greeting8(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview10() {
    BajetiTheme {
        Greeting8("Android")
    }
}
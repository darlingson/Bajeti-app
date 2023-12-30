package com.codeshinobi.bajeti.activities

import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import com.codeshinobi.bajeti.NoPermissionScreen
import com.codeshinobi.bajeti.activities.screens.CameraScreen
import com.codeshinobi.bajeti.activities.ui.theme.BajetiTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

class ScanActivity : ComponentActivity() {
    private lateinit var getContent: ActivityResultLauncher<String>
    val imageUrlState = mutableStateOf<Uri?>(null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Call registerForActivityResult directly in onCreate
        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUrlState.value = it
                Toast.makeText(this, imageUrlState.value.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        setContent {
            var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
            val context = LocalContext.current
            val lifecycleOwner = LocalLifecycleOwner.current
            BajetiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    ScanMainContent(getContent, imageUrlState.value)
                    MainScreen()
                }
            }
        }
    }
}
//@OptIn(ExperimentalComposeUiApi::class)
//@Composable
//fun ScanMainContent(getContent: ActivityResultLauncher<String>, selectedImageUri: Uri?) {
//    var scannedText by remember {
//        mutableStateOf("")
//    }
//    val imageUrl = remember {
//        mutableStateOf<Uri?>(null)
//    }
//    Column(
//        Modifier
//            .fillMaxWidth()
//            .fillMaxHeight(),
//    ) {
//        Column(
//            Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(0.8f),
//        ) {
//            ImageView(selectedImageUri)
//        }
//        Column(
//            Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(),
//        ) {
//            Button(onClick = {
//                getContent.launch("image/*")
//            }) {
//                Text(text = "Select Image")
//            }
//            Button(onClick = { /*TODO*/ }) {
//                Text(text = "Scan Image")
//            }
//            AndroidView(factory = {
//                PreviewView(context).apply {
//                    layoutParams= LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.MATCH_PARENT
//                    )
//                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
//                    scaleType = PreviewView.ScaleType.FILL_CENTER
//                }.also {
//                    startTextRecognition(
//                    )
//                }
//            })
//        }
//    }
//}
@Composable
fun ImageView(selectedImageUri: Uri?) {
    Toast.makeText(LocalContext.current, selectedImageUri.toString(), Toast.LENGTH_SHORT).show()
    Box(modifier = Modifier.fillMaxSize()) {
        val painter: Painter = rememberAsyncImagePainter(model = selectedImageUri)
        Image(painter = painter, contentDescription = "Description of the image")
    }
}
@Preview(showBackground = true)
@Composable
fun ScanPreview() {
    com.codeshinobi.bajeti.ui.theme.BajetiTheme {
//        Greeting("Darlingson")
//        ScanMainContent(,)
    }
}
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen() {

    val cameraPermissionState: PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    MainContent(
        hasPermission = cameraPermissionState.status.isGranted,
        onRequestPermission = cameraPermissionState::launchPermissionRequest
    )
}

@Composable
private fun MainContent(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit
) {

    if (hasPermission) {
        CameraScreen()
    } else {
        NoPermissionScreen(onRequestPermission)
    }
}

@Preview
@Composable
private fun Preview_MainContent() {
    MainContent(
        hasPermission = true,
        onRequestPermission = {}
    )
}
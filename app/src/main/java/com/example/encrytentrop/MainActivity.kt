package com.example.encrytentrop

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.encrytentrop.ui.screens.MainContent
import com.example.encrytentrop.ui.theme.EncrytentropTheme

class MainActivity : ComponentActivity() {
    private lateinit var imageUri: Uri
    private lateinit var imageUriState: MutableState<Uri?>
    private val captureImageLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            imageUriState.value = imageUri
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            imageUriState = remember { mutableStateOf<Uri?>(null) }
            EncrytentropTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent(
                        modifier = Modifier.padding(innerPadding),
                        imageUriState = imageUriState,
                        onCaptureClick = {
                            val uri = createImageUri()
                            imageUri = uri
                            captureImageLauncher.launch(uri)
                        }
                    )
                }
            }
        }
    }

    private fun createImageUri(): Uri {
        val contentResolver = contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "captured_image.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
    }
}
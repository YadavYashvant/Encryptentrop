package com.example.encrytentrop.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.encrytentrop.R
import com.example.encrytentrop.components.NeonButton
import com.example.encrytentrop.components.ScaleButton
import com.example.encrytentrop.utils.calculateEntropy
import com.example.encrytentrop.utils.extractColorsFromImage
import com.example.encrytentrop.utils.generateKeyFromEntropyColorsAndRandomness
import java.io.File
import kotlin.math.log

@Composable
fun MainContent(
    modifier: Modifier,
    imageUriState: MutableState<Uri?>,
    onCaptureClick: () -> Unit
) {
    val context = LocalContext.current
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    var encryptedTextState by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Card {
                    imageUriState.value?.let { uri ->
                        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                        Image(bitmap = bitmap.asImageBitmap(), contentDescription = null)
                    } ?: Text("No image captured")
                }

                Spacer(Modifier.height(16.dp))

                NeonButton(
                    onClick = onCaptureClick,
                    enabled = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_photo_camera_24),
                        contentDescription = null
                    )
                    Spacer(Modifier.padding(8.dp))
                    Text(text = "Capture Entropy")
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        HorizontalDivider(modifier = Modifier
            .height(2.dp)
            .fillMaxWidth())

        Spacer(modifier = Modifier.height(24.dp))

        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.padding(8.dp))

                TextField(
                    value = textState,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { textState = it },
                    label = { Text("Enter text") },
                    trailingIcon = {
                        IconButton(onClick = {
                            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("encryptedText", textState.text)
                            clipboardManager.setPrimaryClip(clip)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_content_copy_24),
                                contentDescription = "Copy to clipboard"
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {

                    }),
                )

                Spacer(Modifier.padding(8.dp))

                ScaleButton(
                    onClick = {
                        imageUriState.value?.let { uri ->
                            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                            val colors = extractColorsFromImage(bitmap)
                            val encryptedText = encryptText(textState.text, colors)
                            textState = TextFieldValue(encryptedText)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_lock_24),
                        contentDescription = null
                    )
                    Spacer(Modifier.padding(8.dp))
                    Text(text = "Encrypt Text")
                }
            }
        }
    }
}

fun encryptText(text: String, colors: List<Int>): String {
    val entropy = calculateEntropy(colors)
    val key = generateKeyFromEntropyColorsAndRandomness(entropy, colors)
    return text.mapIndexed { index, char ->
        char.code.xor(key[index % key.length].code).toChar()
    }.joinToString("")
}
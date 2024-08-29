package com.example.encrytentrop.ui.screens

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.encrytentrop.ui.BottomNavigation

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier,
    imageUriState: MutableState<Uri?>,
    onCaptureClick: () -> Unit
    ) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigation(
                navController = navController
            )
        }
    ) {
        MainContent(
            modifier = modifier,
            imageUriState = imageUriState,
            onCaptureClick = onCaptureClick
        )

    }

}
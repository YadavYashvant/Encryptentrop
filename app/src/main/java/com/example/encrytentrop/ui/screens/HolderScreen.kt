package com.example.encrytentrop.ui.screens

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun HolderScreen(
    modifier: Modifier,
    imageUriState: MutableState<Uri?>,
    onCaptureClick: () -> Unit
){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {

            HomeScreen(
                navController = navController,
                modifier = modifier,
                imageUriState = imageUriState,
                onCaptureClick = onCaptureClick
            )

        }

    }

}
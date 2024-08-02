package com.example.encrytentrop.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun HolderScreen(navController: NavController){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {

            HomeScreen(navController)

        }

    }

}
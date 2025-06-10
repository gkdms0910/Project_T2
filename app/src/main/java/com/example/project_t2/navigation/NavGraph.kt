package com.example.project_t2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.project_t2.screens.MainScreen
import com.example.project_t2.screens.DiaryScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(Modifier,navController) }
        composable("diary") { DiaryScreen() }
    }
}
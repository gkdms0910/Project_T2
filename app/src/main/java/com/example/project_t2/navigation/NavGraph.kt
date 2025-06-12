package com.example.project_t2.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.project_t2.data.DiaryViewModelFactory
import com.example.project_t2.screens.DiaryScreen
import com.example.project_t2.screens.MainScreen
import com.example.project_t2.screens.DiaryListScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    openDrawer: () -> Unit
) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(
            Modifier,
            navController,
            viewModel(factory = DiaryViewModelFactory(LocalContext.current.applicationContext as Application))
        ) }

        composable(
            route = "diary/{diaryId}",
            arguments = listOf(navArgument("diaryId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val diaryId = backStackEntry.arguments?.getInt("diaryId") ?: -1
            DiaryScreen(
                diaryId = diaryId,
                navController = navController,
                openDrawer = openDrawer
            )
        }


        composable("diaryList") {
            DiaryListScreen(navController = navController)
        }
    }
}
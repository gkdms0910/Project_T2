package com.example.project_t2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.project_t2.screens.DiaryScreen
import com.example.project_t2.screens.MainScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(Modifier, navController) }

        // diaryId를 인자로 받도록 수정, 기본값 -1은 '새 일기'를 의미
        composable(
            route = "diary/{diaryId}",
            arguments = listOf(navArgument("diaryId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val diaryId = backStackEntry.arguments?.getInt("diaryId") ?: -1
            DiaryScreen(diaryId = diaryId, navController = navController)
        }
    }
}
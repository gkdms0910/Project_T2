package com.example.project_t2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.project_t2.roomDB.DiaryDatabase
import com.example.project_t2.roomDB.DiaryRepository
import com.example.project_t2.roomDB.DiaryViewModel
import com.example.project_t2.roomDB.DiaryViewModelFactory
import com.example.project_t2.roomDB.WSentiment.WSentimentDatabase
import com.example.project_t2.screens.CalendarSearchScreen
import com.example.project_t2.screens.DiaryScreen
import com.example.project_t2.screens.MainScreen
import com.example.project_t2.screens.SettingScreen
import com.example.project_t2.screens.StatScreen
import com.example.project_t2.ui.theme.AppFontSize
import com.example.project_t2.viewmodel.WSentiment.WSentimentRepository
import com.example.project_t2.viewmodel.stats.StatViewModel
import com.example.project_t2.viewmodel.stats.StatViewModelFactory

@Composable
fun AppNavGraph(
    navController: NavHostController,
    currentFontSize: AppFontSize,
    onFontSizeChange: (AppFontSize) -> Unit
) {
    val context = LocalContext.current
    val diaryDb = DiaryDatabase.getDBInstance(context)
    val diaryRepository = DiaryRepository(diaryDb.getDiaryDao())
    val diaryViewModelFactory = DiaryViewModelFactory(diaryRepository)

    val wSentimentDb = WSentimentDatabase.getDBInstance(context)
    val wSentimentRepository = WSentimentRepository(wSentimentDb)
    val statViewModelFactory = StatViewModelFactory(diaryRepository, wSentimentRepository)

    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController = navController) }
        composable(
            route = "diary?date={date}",
            arguments = listOf(navArgument("date") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val dateString = backStackEntry.arguments?.getString("date")
            val diaryViewModel: DiaryViewModel = viewModel(factory = diaryViewModelFactory)
            DiaryScreen(
                navController = navController,
                viewModel = diaryViewModel,
                dateString = dateString
            )
        }

        composable("calendar_search") {
            val diaryViewModel: DiaryViewModel = viewModel(factory = diaryViewModelFactory)
            CalendarSearchScreen(navController = navController, viewModel = diaryViewModel)
        }

        composable("stats") {
            val statViewModel: StatViewModel = viewModel(factory = statViewModelFactory)
            StatScreen(navController = navController, viewModel = statViewModel)
        }
        composable("settings") {
            SettingScreen(
                navController = navController,
                currentFontSize = currentFontSize,
                onFontSizeChange = onFontSizeChange
            )
        }
    }
}
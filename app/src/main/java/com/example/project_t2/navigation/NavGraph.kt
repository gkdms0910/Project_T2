package com.example.project_t2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.example.project_t2.screens.CalendarSearchScreen
import com.example.project_t2.screens.DiaryScreen
import com.example.project_t2.screens.MainScreen
import com.example.project_t2.screens.SettingScreen
import com.example.project_t2.screens.StatScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    // ViewModel을 생성하기 위한 준비
    val context = LocalContext.current
    val db = DiaryDatabase.getDBInstance(context)
    val repository = DiaryRepository(db.getDiaryDao())
    val factory = DiaryViewModelFactory(repository)

    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(Modifier, navController) }
        composable(
            route = "diary?date={date}",
            arguments = listOf(navArgument("date") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val dateString = backStackEntry.arguments?.getString("date")
            val diaryViewModel: DiaryViewModel = viewModel(factory = factory)
            DiaryScreen(
                navController = navController,
                viewModel = diaryViewModel,
                dateString = dateString
            )
        }

        // 'calendar' 와 'search' 를 'calendar_search' 로 통합
        composable("calendar_search") {
            val diaryViewModel: DiaryViewModel = viewModel(factory = factory)
            CalendarSearchScreen(navController = navController, viewModel = diaryViewModel)
        }

        // 기존 composable("search") 와 composable("calendar")는 삭제

        composable("stats") { StatScreen(navController = navController) }
        composable("settings") { SettingScreen(navController = navController) }
    }
}
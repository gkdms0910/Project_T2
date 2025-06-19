package com.example.project_t2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.project_t2.models.DiaryCalendarScreen
import com.example.project_t2.roomDB.DiaryDatabase
import com.example.project_t2.roomDB.DiaryRepository
import com.example.project_t2.roomDB.DiaryViewModel
import com.example.project_t2.roomDB.DiaryViewModelFactory
import com.example.project_t2.screens.MainScreen
import com.example.project_t2.screens.DiaryScreen
import com.example.project_t2.screens.SearchScreen
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
        composable("diary") {
            val diaryViewModel: DiaryViewModel = viewModel(factory = factory)
            DiaryScreen(navController = navController, viewModel = diaryViewModel)
        }
        composable("calendar") {
            val diaryViewModel: DiaryViewModel = viewModel(factory = factory)
            DiaryCalendarScreen(navController = navController, viewModel = diaryViewModel)
        }
        composable("search") { SearchScreen(navController = navController) }
        composable("stats") { StatScreen(navController = navController) }
        composable("settings") { SettingScreen(navController = navController) }
    }
}
package com.example.project_t2.models

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.project_t2.roomDB.DiaryViewModel

@Composable
fun DiaryCalendarScreen(
    navController: NavController,
    viewModel: DiaryViewModel
) {
    val diaryList by viewModel.diaryList.collectAsState()

    Scaffold(
        topBar = {
            GenericTopAppBar(title = "캘린더", onNavigate = { route -> navController.navigate(route) })
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            CalendarWithMonthNavigation(
                diaryList = diaryList,
                onDateClick = { diary ->
                    // 상세 화면 경로는 추후 구현 필요
                    // navController.navigate("diaryDetail/${diary.id}")
                }
            )
        }
    }
}
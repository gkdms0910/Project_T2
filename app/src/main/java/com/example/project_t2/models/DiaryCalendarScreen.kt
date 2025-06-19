package com.example.project_t2.models

import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.example.project_t2.roomDB.DiaryViewModel

@Composable
fun DiaryCalendarScreen(
    navController: NavController,
    viewModel: DiaryViewModel
) {
    val diaryList by viewModel.diaryList.collectAsState()

    CalendarWithMonthNavigation(
        diaryList = diaryList,
        onDateClick = { diary ->
            navController.navigate("diaryDetail/${diary.id}")
        }
    )
}

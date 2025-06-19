package com.example.p2w.model

import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.p2w.roomDB.DiaryViewModel

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
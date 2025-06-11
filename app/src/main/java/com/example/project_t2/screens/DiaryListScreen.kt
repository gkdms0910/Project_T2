package com.example.project_t2.screens

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project_t2.data.DiaryViewModel
import com.example.project_t2.data.DiaryViewModelFactory
import java.time.format.DateTimeFormatter

@Composable
fun DiaryListScreen(
    navController: NavController,
    diaryViewModel: DiaryViewModel = viewModel(
        factory = DiaryViewModelFactory(LocalContext.current.applicationContext as Application)
    )
) {
    // 화면이 나타날 때마다 일기 목록을 새로고침합니다.
    LaunchedEffect(Unit) {
        diaryViewModel.getAllDiaries()
    }
    val diaryList by diaryViewModel.diaryList.collectAsState()
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("내 일기 목록", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // 일기 목록을 보여주는 부분
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(diaryList) { diary ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // 클릭 시 해당 ID의 일기 수정 화면으로 이동
                            navController.navigate("diary/${diary.id}")
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = diary.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = diary.content,
                            fontSize = 14.sp,
                            maxLines = 2, // 내용은 최대 2줄만 보이게
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = diary.time.format(formatter),
                            fontSize = 12.sp,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }
    }
}
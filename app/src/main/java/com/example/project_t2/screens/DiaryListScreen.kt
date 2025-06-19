package com.example.project_t2.screens

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_t2.R
import com.example.project_t2.data.DiaryViewModel
import com.example.project_t2.data.DiaryViewModelFactory
import com.example.project_t2.graphics.Emotion
import com.example.project_t2.models.Weathers
import com.example.project_t2.roomDB.DiaryEntity
import com.example.project_t2.ui.theme.Project_T2Theme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 로직과 상태를 관리하는 Stateful 컴포저블
 */
@Composable
fun DiaryListScreen(
    navController: NavController,
    diaryViewModel: DiaryViewModel = viewModel(
        factory = DiaryViewModelFactory(LocalContext.current.applicationContext as Application)
    )
    // [삭제] openDrawer 파라미터 삭제
) {
    LaunchedEffect(Unit) {
        diaryViewModel.getAllDiaries()
    }
    val diaryList by diaryViewModel.diaryList.collectAsState()

    val showDialog = remember { mutableStateOf(false) }
    val diaryToDelete = remember { mutableStateOf<DiaryEntity?>(null) }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("일기 삭제") },
            text = { Text("정말로 이 일기를 삭제하시겠습니까?") },
            confirmButton = {
                TextButton(onClick = {
                    diaryToDelete.value?.let { diaryViewModel.deleteDiary(it) }
                    showDialog.value = false
                }) { Text("삭제") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) { Text("취소") }
            }
        )
    }

    DiaryListScreenContent(
        navController = navController, // [추가] NavController 전달
        diaries = diaryList,
        onDiaryClick = { diaryId ->
            navController.navigate("diary/$diaryId")
        },
        onDeleteClick = { diary ->
            diaryToDelete.value = diary
            showDialog.value = true
        }
        // [삭제] onMenuClick 삭제
    )
}

/**
 * UI만 담당하는 Stateless 컴포저블
 */
@Composable
private fun DiaryListScreenContent(
    navController: NavController, // [추가] NavController 받기
    diaries: List<DiaryEntity>,
    onDiaryClick: (Int) -> Unit,
    onDeleteClick: (DiaryEntity) -> Unit
    // [삭제] onMenuClick 파라미터 삭제
) {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // [수정] 기존 Image를 MenuDropdown으로 교체
            MenuDropdown(navController = navController)
            Text("내 일기 목록", fontSize = 24.sp, fontWeight = FontWeight.Bold)


        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(diaries) { diary ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDiaryClick(diary.id) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = diary.title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = diary.content, fontSize = 14.sp, maxLines = 2, color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = diary.time.format(formatter), fontSize = 12.sp, color = Color.LightGray)
                        }
                        IconButton(onClick = { onDeleteClick(diary) }) {
                            Icon(Icons.Default.Delete, contentDescription = "삭제")
                        }
                    }
                }
            }
        }
    }
}

/**
 * 간단해진 프리뷰
 */
@Preview(showBackground = true)
@Composable
private fun DiaryListScreenPreview() {
    val dummyDiaries = listOf(
        DiaryEntity(id = 1, title = "첫 번째 일기", content = "오늘은 정말 좋은 날이었다. 코딩이 술술 잘 풀렸다.", weather = Weathers.SUNNY, emotion = Emotion.HAPPY, time = LocalDateTime.now()),
        DiaryEntity(id = 2, title = "두 번째 일기", content = "조금 피곤했지만 보람찬 하루. 버그를 하나 잡았다.조금 피곤했지만 보람찬 하루. 버그를 하나 잡았다.조금 피곤했지만 보람찬 하루. 버그를 하나 잡았다.조금 피곤했지만 보람찬 하루. 버그를 하나 잡았다.", weather = Weathers.CLOUDY, emotion = Emotion.TENDER, time = LocalDateTime.now().minusDays(1))
    )
    Project_T2Theme {
        DiaryListScreenContent(
            navController = rememberNavController(), // [추가] 프리뷰용 NavController
            diaries = dummyDiaries,
            onDiaryClick = {},
            onDeleteClick = {}
            // [삭제] onMenuClick 삭제
        )
    }
}
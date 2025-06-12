package com.example.project_t2.screens

import android.app.Application
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project_t2.data.DiaryViewModel
import com.example.project_t2.data.DiaryViewModelFactory
import com.example.project_t2.roomDB.DiaryEntity
import java.time.format.DateTimeFormatter

@Composable
fun DiaryListScreen(
    navController: NavController,
    diaryViewModel: DiaryViewModel = viewModel(
        factory = DiaryViewModelFactory(LocalContext.current.applicationContext as Application)
    )
) {
    LaunchedEffect(Unit) {
        diaryViewModel.getAllDiaries()
    }
    val diaryList by diaryViewModel.diaryList.collectAsState()
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")

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
                }) {
                    Text("삭제")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("취소")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("내 일기 목록", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(diaryList) { diary ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("diary/${diary.id}")
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = diary.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = diary.content,
                                fontSize = 14.sp,
                                maxLines = 2,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = diary.time.format(formatter),
                                fontSize = 12.sp,
                                color = Color.LightGray
                            )
                        }
                        IconButton(onClick = {
                            diaryToDelete.value = diary
                            showDialog.value = true
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "삭제")
                        }
                    }
                }
            }
        }
    }
}
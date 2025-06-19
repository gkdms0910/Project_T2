package com.example.project_t2.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project_t2.models.AppBackground
import com.example.project_t2.models.CalendarWithMonthNavigation
import com.example.project_t2.models.DiaryCard
import com.example.project_t2.models.GenericTopAppBar
import com.example.project_t2.models.SortButtons
import com.example.project_t2.roomDB.DiaryEntity
import com.example.project_t2.roomDB.DiaryViewModel
import com.example.project_t2.roomDB.SortType
import com.example.project_t2.ui.theme.MainFont
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarSearchScreen(
    navController: NavController,
    viewModel: DiaryViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var sortType by remember { mutableStateOf(SortType.NEWEST) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }
    var bottomSheetDiaries by remember { mutableStateOf<List<DiaryEntity>>(emptyList()) }
    var bottomSheetTitle by remember { mutableStateOf("") }

    val diaryList by viewModel.diaryList.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val performSearch: () -> Unit = {
        coroutineScope.launch {
            if (searchQuery.isNotBlank()) {
                val results = viewModel.repository.searchDiary(searchQuery, sortType)
                bottomSheetDiaries = results
                bottomSheetTitle = "'$searchQuery' 검색 결과"
                if (results.isNotEmpty()) {
                    showBottomSheet = true
                }
            }
        }
    }

    AppBackground {
        Scaffold(
            topBar = {
                GenericTopAppBar(title = "캘린더/검색", onNavigate = { route -> navController.navigate(route) })
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("검색어를 입력하세요", fontFamily = MainFont) },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = performSearch) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    textStyle = TextStyle(fontFamily = MainFont)
                )

                Spacer(modifier = Modifier.height(8.dp))

                SortButtons(current = sortType, onSortChange = {
                    sortType = it
                    if (searchQuery.isNotBlank()) {
                        performSearch()
                    }
                })

                Spacer(modifier = Modifier.height(16.dp))

                CalendarWithMonthNavigation(
                    diaryList = diaryList,
                    onDateClick = { diary ->
                        bottomSheetDiaries = listOf(diary)
                        bottomSheetTitle = "'${diary.time.toLocalDate()}'의 일기"
                        showBottomSheet = true
                    }
                )

                // 추가된 문구
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "달력의 날짜를 누르거나, 키워드로 일기를 검색해 보세요.",
                        fontFamily = MainFont,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                    containerColor = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .navigationBarsPadding()
                    ) {
                        Text(bottomSheetTitle, style = MaterialTheme.typography.titleLarge, fontFamily = MainFont)
                        Spacer(modifier = Modifier.height(16.dp))

                        if (bottomSheetDiaries.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("해당하는 일기가 없습니다.", fontFamily = MainFont)
                            }
                        } else {
                            LazyColumn {
                                items(bottomSheetDiaries) { diary ->
                                    DiaryCard(diary = diary) {
                                        coroutineScope.launch {
                                            sheetState.hide()
                                        }.invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                showBottomSheet = false
                                            }
                                            navController.navigate("diary?date=${diary.time.toLocalDate()}")
                                        }
                                    }
                                    HorizontalDivider(color = Color.LightGray)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
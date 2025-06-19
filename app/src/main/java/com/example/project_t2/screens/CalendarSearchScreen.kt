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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project_t2.models.CalendarWithMonthNavigation
import com.example.project_t2.models.DiaryCard
import com.example.project_t2.models.GenericTopAppBar
import com.example.project_t2.models.SortButtons
import com.example.project_t2.roomDB.DiaryEntity
import com.example.project_t2.roomDB.DiaryViewModel
import com.example.project_t2.roomDB.SortType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarSearchScreen(
    navController: NavController,
    viewModel: DiaryViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var sortType by remember { mutableStateOf(SortType.NEWEST) }

    // Bottom Sheet를 위한 상태 변수들
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }
    var bottomSheetDiaries by remember { mutableStateOf<List<DiaryEntity>>(emptyList()) }
    var bottomSheetTitle by remember { mutableStateOf("") }

    val diaryList by viewModel.diaryList.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // 키워드 검색 실행 함수 (반환 타입을 Unit으로 명시하여 오류 수정)
    val performSearch: () -> Unit = {
        coroutineScope.launch {
            if (searchQuery.isNotBlank()) {
                val results = viewModel.repository.searchDiary(searchQuery, sortType)
                bottomSheetDiaries = results
                bottomSheetTitle = "'$searchQuery' 검색 결과"
                showBottomSheet = true // 검색 후 Bottom Sheet 표시
            }
        }
    }

    Scaffold(
        topBar = {
            GenericTopAppBar(title = "캘린더/검색", onNavigate = { route -> navController.navigate(route) })
        }
    ) { innerPadding ->
        // 메인 화면 UI (검색창, 캘린더 등)
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("검색어를 입력하세요") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = performSearch) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
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
                    // 날짜 클릭 시 해당 날짜의 일기를 Bottom Sheet에 표시
                    bottomSheetDiaries = listOf(diary)
                    bottomSheetTitle = "'${diary.time.toLocalDate()}'의 일기"
                    showBottomSheet = true
                }
            )

            // 화면 중앙에 안내 텍스트 표시
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("날짜를 선택하거나 키워드를 검색하여 일기를 확인하세요.")
            }
        }

        // Bottom Sheet UI 정의
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
            ) {
                // Bottom Sheet 내부 UI
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .navigationBarsPadding() // 하단 네비게이션 바와의 간섭 방지
                ) {
                    // 제목 표시
                    Text(bottomSheetTitle, style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))

                    // 일기 목록 또는 결과 없음 메시지 표시
                    if (bottomSheetDiaries.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("해당하는 일기가 없습니다.")
                        }
                    } else {
                        LazyColumn {
                            items(bottomSheetDiaries) { diary ->
                                DiaryCard(diary = diary) {
                                    coroutineScope.launch {
                                        sheetState.hide() // 화면 이동 전 Sheet 숨기기
                                    }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                        // 상세 화면으로 이동
                                        navController.navigate("diary?date=${diary.time.toLocalDate()}")
                                    }
                                }
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}
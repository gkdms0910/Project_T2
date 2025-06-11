package com.example.project_t2.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project_t2.R
import com.example.project_t2.data.DiaryViewModel
import com.example.project_t2.data.DiaryViewModelFactory
import com.example.project_t2.data.WeatherViewModel
import com.example.project_t2.graphics.Emotion
import com.example.project_t2.models.Weathers
import com.example.project_t2.roomDB.DiaryEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.app.Application

@Composable
fun DiaryScreen(
    diaryId: Int,
    navController: NavController,
    diaryViewModel: DiaryViewModel = viewModel(
        factory = DiaryViewModelFactory(LocalContext.current.applicationContext as Application)
    ),
    weatherViewModel: WeatherViewModel = viewModel()
) {
    // UI 상태 관리 변수
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedEmotion by remember { mutableStateOf(Emotion.SMILE) }

    // DB에서 불러온 일기 원본 (수정 시 비교용)
    val savedDiary by diaryViewModel.selectedDiary.collectAsState()

    // 날씨 정보 상태
    val weatherState by weatherViewModel.weatherData.collectAsState()

    // LaunchedEffect: diaryId가 변경되거나 화면이 처음 로드될 때 실행
    LaunchedEffect(key1 = diaryId) {
        if (diaryId != -1) {
            // 기존 일기 수정
            diaryViewModel.getDiary(diaryId)
        }
        // 날씨 정보 요청 (항상 현재 날씨 기준)
        val current = LocalDateTime.now()
        val baseTime = if (current.minute < 40) {
            current.minusHours(1).format(DateTimeFormatter.ofPattern("HH00"))
        } else {
            current.format(DateTimeFormatter.ofPattern("HH00"))
        }
        val baseDate = current.format(DateTimeFormatter.ofPattern("yyyyMMdd"))

        // nx, ny는 현재 위치 기반으로 수정해야 하지만, 여기서는 서울시청 좌표로 유지
        weatherViewModel.loadWeather(
            baseDate = baseDate,
            baseTime = baseTime,
            nx = 60,
            ny = 127
        )
    }

    // DB에서 일기 정보를 성공적으로 불러왔을 때, UI 상태를 업데이트
    LaunchedEffect(key1 = savedDiary) {
        savedDiary?.let {
            title = it.title
            content = it.content
            selectedEmotion = it.emotion
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.paper_texture),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // 상단 바 (메뉴, 저장 버튼)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.outline_menu_24),
                    contentDescription = "Menu",
                    modifier = Modifier.size(50.dp)
                )
                // 저장(체크) 버튼
                Image(
                    painter = painterResource(id = R.drawable.outline_check_circle_24),
                    contentDescription = "Save",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            // 현재 날씨 정보 가져오기 (예시: 맑음)
                            val currentWeather = Weathers.SUNNY // 실제로는 weatherState에서 파싱해야 함

                            val diary = if (diaryId == -1) {
                                // 새 일기 저장
                                DiaryEntity(
                                    title = title,
                                    weather = currentWeather,
                                    emotion = selectedEmotion,
                                    content = content
                                )
                            } else {
                                // 기존 일기 수정
                                DiaryEntity(
                                    id = diaryId,
                                    title = title,
                                    weather = savedDiary?.weather ?: currentWeather, // 날씨는 수정 불가
                                    emotion = selectedEmotion,
                                    content = content,
                                    time = savedDiary?.time ?: LocalDateTime.now() // 생성 시간은 유지
                                )
                            }

                            if (diaryId == -1) {
                                diaryViewModel.insertDiary(diary)
                            } else {
                                diaryViewModel.updateDiary(diary)
                            }
                            // 저장 후 이전 화면으로 돌아가기
                            navController.popBackStack()
                        }
                )
            }

            // TODO: 날씨, 감정 선택 UI 영역 (이전 답변 참고하여 구현)

            // 일기 작성 영역 (제목 + 내용)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // 남은 공간을 모두 차지하도록
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // 조건 1: 제목과 내용이 함께 스크롤되도록 LazyColumn 안에 배치
                item {
                    // 제목 입력 필드
                    BasicTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                        cursorBrush = SolidColor(Color.Black),
                        decorationBox = { innerTextField ->
                            if (title.isEmpty()) {
                                Text("제목", style = TextStyle(color = Color.Gray, fontSize = 22.sp, fontWeight = FontWeight.Bold))
                            }
                            innerTextField()
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    // 내용 입력 필드
                    BasicTextField(
                        value = content,
                        onValueChange = { content = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 400.dp), // 최소 높이 지정
                        textStyle = TextStyle(fontSize = 16.sp, lineHeight = 24.sp),
                        cursorBrush = SolidColor(Color.Black),
                        decorationBox = { innerTextField ->
                            if (content.isEmpty()) {
                                Text("오늘 하루는 어땠나요?", style = TextStyle(color = Color.Gray, fontSize = 16.sp))
                            }
                            innerTextField()
                        }
                    )
                }
            }
        }
    }
}
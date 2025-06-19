package com.example.project_t2.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.project_t2.R
import com.example.project_t2.data.GetWeather
import com.example.project_t2.data.WeatherAnalyzer
import com.example.project_t2.data.WeatherData
import com.example.project_t2.graphics.Emotion
import com.example.project_t2.models.Weathers
import com.example.project_t2.network.getKoBERTResponse
import com.example.project_t2.roomDB.DiaryEntity
import com.example.project_t2.roomDB.DiaryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

private fun mapKobertToEmotion(kobertLabel: String): Emotion {
    // KoBERT가 반환하는 레이블과 Enum의 displayName이 직접 일치하는지 확인
    val directMatch = Emotion.values().find { it.displayName == kobertLabel }
    if (directMatch != null) {
        return directMatch
    }

    // 직접 일치하지 않을 경우, 예상되는 다른 레이블 값들로 매핑 시도
    return when (kobertLabel) {
        "행복"-> Emotion.HAPPY
        "화남" -> Emotion.ANGRY
        "슬픔" -> Emotion.SAD
        "두려움"-> Emotion.FEAR
        "중립" -> Emotion.TENDER //
        "지루함" -> Emotion.BORED
        "미소" -> Emotion.SMILE
        "놀람" -> Emotion.FEAR //
        else -> Emotion.SMILE //
    }
}

@Composable
fun DiaryScreen(
    navController: NavController,
    viewModel: DiaryViewModel,
    dateString: String? = null,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedEmotion by remember { mutableStateOf<Emotion?>(null) }
    var selectedWeather by remember { mutableStateOf<Weathers?>(null) }
    var weatherDescription by remember { mutableStateOf("날씨 정보 로딩 중...") }

    var sky by remember { mutableStateOf<Int?>(null) }
    var t1h by remember { mutableStateOf<Double?>(null) }
    var pty by remember { mutableStateOf<Int?>(null) }

    var diaryId by remember { mutableStateOf<Int?>(null) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val diaryDate = remember {
        try {
            dateString?.let { LocalDate.parse(it) } ?: LocalDate.now()
        } catch (e: DateTimeParseException) {
            LocalDate.now()
        }
    }

    val isToday = diaryDate.isEqual(LocalDate.now())
    var isEditMode by remember { mutableStateOf(false) }
    var diaryExists by remember { mutableStateOf(false) }
    var isAnalyzingEmotion by remember { mutableStateOf(false) }


    LaunchedEffect(diaryDate) {
        val existingDiary = viewModel.getDiaryByDate(diaryDate)
        if (existingDiary != null) {
            diaryId = existingDiary.id
            title = existingDiary.title
            content = existingDiary.content
            selectedEmotion = existingDiary.emotion
            selectedWeather = existingDiary.weather
            diaryExists = true
            isEditMode = false // 기존 일기는 항상 뷰 모드로 시작
        } else {
            // 새 일기
            diaryId = null
            title = ""
            content = ""
            selectedEmotion = null
            selectedWeather = null
            diaryExists = false
            isEditMode = isToday // 오늘 날짜의 새 일기만 바로 편집 모드
        }

        if (isToday) {
            withContext(Dispatchers.IO) {
                try {
                    val items = GetWeather()
                    sky = items.find { it.category == "SKY" }?.fcstValue?.toIntOrNull()
                    pty = items.find { it.category == "PTY" }?.fcstValue?.toIntOrNull()
                    t1h = items.find { it.category == "T1H" }?.fcstValue?.toDoubleOrNull()
                    val currentWeatherData = WeatherData(sky, pty, t1h)
                    weatherDescription = WeatherAnalyzer.analyze(currentWeatherData)
                } catch (e: Exception) {
                    weatherDescription = "날씨를 불러오지 못했어요."
                }
            }
        } else {
            // 과거 날짜의 날씨는 보여주지 않음
            weatherDescription = "작성된 일기"
        }
    }

    // 내용이 변경될 때마다 디바운싱을 통해 감정 분석 실행
    LaunchedEffect(content) {
        if (content.isBlank() || !isEditMode) {
            return@LaunchedEffect
        }

        delay(1000L) // 사용자가 1초간 입력을 멈추면 분석 시작

        isAnalyzingEmotion = true
        try {
            val kobertResponse = withContext(Dispatchers.IO) {
                getKoBERTResponse(content)
            }
            // API가 반환하는 실제 값을 확인하기 위해 로그 추가
            Log.d("EmotionAnalysis", "KoBERT API Response Label: '${kobertResponse.predicted_label}'")

            selectedEmotion = mapKobertToEmotion(kobertResponse.predicted_label)
        } catch (e: Exception) {
            Log.e("DiaryScreen", "Failed to analyze emotion", e)
        } finally {
            isAnalyzingEmotion = false
        }
    }


    // 오늘이 아닌데 일기가 없으면 작성 불가
    if (!isToday && !diaryExists) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("지난 날짜의 일기는 작성할 수 없습니다.")
        }
        return
    }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.paper_texture),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DiaryTopAppBar(
                onNavigate = { route -> navController.navigate(route) },
                isEditMode = isEditMode,
                isToday = isToday,
                diaryExists = diaryExists,
                onEditClick = { isEditMode = true },
                onSaveClick = {
                    coroutineScope.launch {
                        if (selectedWeather == null || selectedEmotion == null) {
                            Toast.makeText(context, "날씨와 감정을 선택해주세요.", Toast.LENGTH_SHORT).show()
                            return@launch
                        }

                        // ID가 있으면 업데이트, 없으면 삽입
                        val diary = DiaryEntity(
                            id = diaryId ?: 0,
                            title = title,
                            content = content,
                            emotion = selectedEmotion!!,
                            weather = selectedWeather!!,
                            time = diaryDate.atTime(LocalDateTime.now().toLocalTime()) // 날짜는 유지, 시간은 현재
                        )

                        if (diaryId != null) {
                            viewModel.updateDiary(diary)
                        } else {
                            viewModel.insertDiary(diary)
                        }
                        navController.popBackStack()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("제목") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditMode,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            WeatherSelector(
                weatherDescription = weatherDescription,
                selectedWeather = selectedWeather,
                onWeatherSelected = { selectedWeather = it },
                isEditable = isEditMode
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 감정 분석 중일 때 로딩 아이콘 표시
            if (isAnalyzingEmotion) {
                CircularProgressIndicator(modifier = Modifier.size(50.dp))
            } else {
                EmotionSelector(
                    selectedEmotion = selectedEmotion,
                    onEmotionSelected = { selectedEmotion = it },
                    isEditable = isEditMode
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("오늘의 이야기를 들려주세요.") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                enabled = isEditMode,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun WeatherSelector(
    weatherDescription: String,
    selectedWeather: Weathers?,
    onWeatherSelected: (Weathers) -> Unit,
    isEditable: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "날씨: $weatherDescription", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Weathers.values().forEach { weather ->
                val isSelected = weather == selectedWeather
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color.LightGray.copy(alpha = 0.5f) else Color.Transparent)
                        .clickable(enabled = isEditable) { onWeatherSelected(weather) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = weatherToEmoji(weather), fontSize = 30.sp)
                }
            }
        }
    }
}

fun weatherToEmoji(weather: Weathers): String {
    return when (weather) {
        Weathers.SUNNY -> "☀️"
        Weathers.CLOUDY -> "☁️"
        Weathers.PARTLY_CLOUDY -> "⛅"
        Weathers.RAINY -> "🌧️"
        Weathers.SNOWY -> "🌨️"
        Weathers.STORMY -> "⛈️"
        Weathers.FOGGY -> "🌫️"
        Weathers.WINDY -> "🌬️"
        Weathers.HAZY -> "😵"
    }
}

@Composable
fun EmotionSelector(
    selectedEmotion: Emotion?,
    onEmotionSelected: (Emotion) -> Unit,
    isEditable: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Emotion.values().forEach { emotion ->
            val isSelected = emotion == selectedEmotion
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Color.LightGray.copy(alpha = 0.5f) else Color.Transparent)
                    .clickable(enabled = isEditable) { onEmotionSelected(emotion) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = emotion.emoji, fontSize = 32.sp)
            }
        }
    }
}

@Composable
fun DiaryTopAppBar(
    onNavigate: (String) -> Unit,
    isEditMode: Boolean,
    isToday: Boolean,
    diaryExists: Boolean,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.outline_menu_24),
                contentDescription = "Menu",
                modifier = Modifier
                    .size(50.dp)
                    .clickable { menuExpanded = true }
            )
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("캘린더/검색") },
                    onClick = {
                        menuExpanded = false
                        onNavigate("calendar_search")
                    }
                )
                DropdownMenuItem(
                    text = { Text("통계") },
                    onClick = {
                        menuExpanded = false
                        onNavigate("stats")
                    }
                )
                DropdownMenuItem(
                    text = { Text("설정") },
                    onClick = {
                        menuExpanded = false
                        onNavigate("settings")
                    }
                )
            }
        }
        Text("일기", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Box(modifier = Modifier.size(50.dp)) {
            if (isToday) {
                if (isEditMode) {
                    // 편집 모드일 때 저장 버튼
                    Image(
                        painter = painterResource(id = R.drawable.outline_check_circle_24),
                        contentDescription = "Save",
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { onSaveClick() }
                    )
                } else if (diaryExists) {
                    // 뷰 모드이고 오늘 일기가 존재할 때 수정 버튼
                    Image(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "Edit",
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { onEditClick() }
                    )
                }
            }
            // 지난 날짜의 일기는 뷰 모드에서 아무 아이콘도 보이지 않음
        }
    }
}
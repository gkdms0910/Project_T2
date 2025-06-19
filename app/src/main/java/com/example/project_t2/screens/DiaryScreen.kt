package com.example.project_t2.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
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
import com.example.project_t2.roomDB.DiaryEntity
import com.example.project_t2.roomDB.DiaryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun DiaryScreen(
    navController: NavController,
    viewModel: DiaryViewModel,
    modifier: Modifier = Modifier,
    dateString: String?
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedEmotion by remember { mutableStateOf<Emotion?>(null) }
    var selectedWeather by remember { mutableStateOf<Weathers?>(null) }
    var weatherDescription by remember { mutableStateOf("날씨 정보 로딩 중...") }

    var diaryId by remember { mutableStateOf<Int?>(null) }
    var isReadOnly by remember { mutableStateOf(false) }
    var originalTime by remember { mutableStateOf<LocalDateTime?>(null) }

    val targetDate = remember(dateString) {
        dateString?.let { LocalDate.parse(it) } ?: LocalDate.now()
    }
    val isToday = remember(targetDate) {
        targetDate == LocalDate.now()
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(targetDate) {
        val diaryForDate = viewModel.getDiaryByDate(targetDate)
        if (diaryForDate != null) {
            diaryId = diaryForDate.id
            title = diaryForDate.title
            content = diaryForDate.content
            selectedEmotion = diaryForDate.emotion
            selectedWeather = diaryForDate.weather
            originalTime = diaryForDate.time
            isReadOnly = true
        } else {
            diaryId = null
            title = ""
            content = ""
            selectedEmotion = null
            selectedWeather = null
            originalTime = null
            isReadOnly = false
        }

        // 날씨 정보 불러오는 로직 수정
        withContext(Dispatchers.IO) {
            try {
                val items = GetWeather()
                // PTY(강수형태)와 SKY(하늘상태) 값을 가져옵니다.
                val pty = items.find { it.category == "PTY" }?.fcstValue?.toIntOrNull()
                val sky = items.find { it.category == "SKY" }?.fcstValue?.toIntOrNull()

                // 새로 정의된 WeatherData와 WeatherAnalyzer.analyze 함수를 호출합니다.
                val currentWeatherData = WeatherData(sky = sky, pty = pty)
                weatherDescription = WeatherAnalyzer.analyze(currentWeatherData)

            } catch (e: Exception) {
                weatherDescription = "날씨를 불러오지 못했어요."
            }
        }
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
                onSaveClick = {
                    coroutineScope.launch {
                        if (selectedWeather == null || selectedEmotion == null) {
                            Toast.makeText(context, "날씨와 감정을 선택해주세요.", Toast.LENGTH_SHORT).show()
                            return@launch
                        }

                        val diary = DiaryEntity(
                            id = diaryId ?: 0,
                            title = title,
                            content = content,
                            emotion = selectedEmotion!!,
                            weather = selectedWeather!!,
                            time = originalTime ?: LocalDateTime.of(targetDate, LocalTime.now())
                        )

                        if (diaryId != null) {
                            viewModel.updateDiary(diary)
                        } else {
                            viewModel.insertDiary(diary)
                        }

                        Toast.makeText(context, "일기가 저장되었습니다.", Toast.LENGTH_SHORT).show()

                        val savedDiary = viewModel.getDiaryByDate(targetDate)
                        if(savedDiary != null) {
                            diaryId = savedDiary.id
                            originalTime = savedDiary.time
                            isReadOnly = true
                        }
                    }
                },
                onEditClick = { isReadOnly = false },
                isReadOnly = isReadOnly,
                isExistingDiary = diaryId != null,
                isEditable = isToday
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("제목") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isReadOnly,
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
                isReadOnly = isReadOnly
            )

            Spacer(modifier = Modifier.height(16.dp))

            EmotionSelector(
                selectedEmotion = selectedEmotion,
                onEmotionSelected = { selectedEmotion = it },
                isReadOnly = isReadOnly
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("오늘의 이야기를 들려주세요.") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                enabled = !isReadOnly,
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
fun DiaryTopAppBar(
    onNavigate: (String) -> Unit,
    onSaveClick: () -> Unit,
    onEditClick: () -> Unit,
    isReadOnly: Boolean,
    isExistingDiary: Boolean,
    isEditable: Boolean
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
        Text("일기 작성", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        if (isExistingDiary && isReadOnly) {
            if (isEditable) {
                Image(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Edit",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable { onEditClick() }
                )
            } else {
                Spacer(modifier = Modifier.size(50.dp))
            }
        } else {
            Image(
                painter = painterResource(id = R.drawable.outline_check_circle_24),
                contentDescription = "Save",
                modifier = Modifier
                    .size(50.dp)
                    .clickable { onSaveClick() }
            )
        }
    }
}

@Composable
fun WeatherSelector(
    weatherDescription: String,
    selectedWeather: Weathers?,
    onWeatherSelected: (Weathers) -> Unit,
    isReadOnly: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "오늘의 날씨: $weatherDescription", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Weathers.values().forEach { weather ->
                val isSelected = weather == selectedWeather
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color.LightGray.copy(alpha = 0.5f) else Color.Transparent)
                        .clickable(enabled = !isReadOnly) { onWeatherSelected(weather) },
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
        Weathers.HAZY -> "😶‍🌫️"
    }
}


@Composable
fun EmotionSelector(selectedEmotion: Emotion?, onEmotionSelected: (Emotion) -> Unit, isReadOnly: Boolean) {
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
                    .clickable(enabled = !isReadOnly) { onEmotionSelected(emotion) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = emotion.emoji, fontSize = 32.sp)
            }
        }
    }
}
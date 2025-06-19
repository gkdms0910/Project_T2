package com.example.project_t2.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.project_t2.R
import com.example.project_t2.data.GetWeather
import com.example.project_t2.data.WeatherAnalyzer
import com.example.project_t2.data.WeatherData
import com.example.project_t2.graphics.Emotion
import com.example.project_t2.models.AppBackground
import com.example.project_t2.models.Weathers
import com.example.project_t2.network.getKoBERTResponse
import com.example.project_t2.roomDB.DiaryEntity
import com.example.project_t2.roomDB.DiaryViewModel
import com.example.project_t2.ui.theme.MainFont
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

private fun mapKobertToEmotion(kobertLabel: String): Emotion {
    return when (kobertLabel) {
        "행복" -> Emotion.HAPPY
        "미소" -> Emotion.JOY
        "슬픔" -> Emotion.SAD
        "화남", "두려움", "지루함" -> Emotion.BAD
        "중립" -> Emotion.TENDER
        else -> Emotion.TENDER
    }
}

private fun mapDescriptionToWeather(description: String): Weathers {
    return when {
        "맑음" in description -> Weathers.SUNNY
        "흐림" in description -> Weathers.CLOUDY
        "구름" in description -> Weathers.PARTLY_CLOUDY
        "비" in description -> Weathers.RAINY
        "눈" in description -> Weathers.SNOWY
        "소나기" in description -> Weathers.RAINY
        "폭풍" in description -> Weathers.STORMY
        else -> Weathers.SUNNY // 기본값
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
            isEditMode = false
        } else {
            diaryId = null
            title = ""
            content = ""
            selectedEmotion = Emotion.TENDER
            selectedWeather = null
            diaryExists = false
            isEditMode = isToday
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

                    withContext(Dispatchers.Main) {
                        selectedWeather = mapDescriptionToWeather(weatherDescription)
                    }

                } catch (e: Exception) {
                    weatherDescription = "날씨를 불러오지 못했어요."
                }
            }
        } else {
            weatherDescription = if (diaryExists) "작성된 일기" else "지난 날짜"
        }
    }

    LaunchedEffect(content) {
        if (content.isBlank() || !isEditMode) {
            return@LaunchedEffect
        }

        delay(1000L)

        isAnalyzingEmotion = true
        try {
            val kobertResponse = withContext(Dispatchers.IO) {
                getKoBERTResponse(content)
            }
            Log.d("EmotionAnalysis", "KoBERT API Response Label: '${kobertResponse.predicted_label}'")
            selectedEmotion = mapKobertToEmotion(kobertResponse.predicted_label)
        } catch (e: Exception) {
            Log.e("DiaryScreen", "Failed to analyze emotion", e)
        } finally {
            isAnalyzingEmotion = false
        }
    }


    if (!isToday && !diaryExists) {
        AppBackground {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("지난 날짜의 일기는 작성할 수 없습니다.", fontFamily = MainFont)
            }
        }
        return
    }

    AppBackground {
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

                        val diary = DiaryEntity(
                            id = diaryId ?: 0,
                            title = title,
                            content = content,
                            emotion = selectedEmotion!!,
                            weather = selectedWeather!!,
                            time = diaryDate.atTime(LocalDateTime.now().toLocalTime())
                        )

                        if (diaryId != null) {
                            viewModel.updateDiary(diary)
                        } else {
                            viewModel.insertDiary(diary)
                        }

                        val savedDiary = viewModel.getDiaryByDate(diaryDate)
                        if (savedDiary != null) {
                            diaryId = savedDiary.id
                            diaryExists = true
                        }

                        isEditMode = false
                        Toast.makeText(context, "일기가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("제목", fontFamily = MainFont) },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditMode,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                ),
                textStyle = TextStyle(fontFamily = MainFont)
            )

            Spacer(modifier = Modifier.height(16.dp))

            WeatherSelector(
                weatherDescription = weatherDescription,
                selectedWeather = selectedWeather,
                onWeatherSelected = { selectedWeather = it },
                isEditable = isEditMode
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                label = { Text("오늘의 이야기를 들려주세요.", fontFamily = MainFont) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                enabled = isEditMode,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                ),
                textStyle = TextStyle(fontFamily = MainFont)
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
        Text(text = "날씨: $weatherDescription", fontWeight = FontWeight.SemiBold, fontFamily = MainFont)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Weathers.values().forEach { weather ->
                val isSelected = weather == selectedWeather
                val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(width = 2.dp, color = borderColor, shape = CircleShape)
                        .background(color = backgroundColor, shape = CircleShape)
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
            val scale = if (isSelected) 1.2f else 1.0f
            val alpha = if (isSelected) 1f else 0.6f

            Image(
                painter = painterResource(id = emotion.imageResId),
                contentDescription = emotion.displayName,
                modifier = Modifier
                    .size(50.dp)
                    .scale(scale)
                    .alpha(alpha)
                    .clip(CircleShape)
                    .clickable(enabled = isEditable) { onEmotionSelected(emotion) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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

    CenterAlignedTopAppBar(
        title = { Text("일기", fontFamily = MainFont) },
        navigationIcon = {
            IconButton(onClick = { menuExpanded = true }) {
                Image(
                    painter = painterResource(id = R.drawable.outline_menu_24),
                    contentDescription = "Menu"
                )
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("캘린더/검색", fontFamily = MainFont) },
                    onClick = {
                        menuExpanded = false
                        onNavigate("calendar_search")
                    }
                )
                DropdownMenuItem(
                    text = { Text("통계", fontFamily = MainFont) },
                    onClick = {
                        menuExpanded = false
                        onNavigate("stats")
                    }
                )
                DropdownMenuItem(
                    text = { Text("설정", fontFamily = MainFont) },
                    onClick = {
                        menuExpanded = false
                        onNavigate("settings")
                    }
                )
            }
        },
        actions = {
            if (isToday || diaryExists) {
                if (isEditMode) {
                    IconButton(onClick = onSaveClick) {
                        Image(
                            painter = painterResource(id = R.drawable.outline_check_box_24),
                            contentDescription = "Save"
                        )
                    }
                } else {
                    IconButton(onClick = onEditClick) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = "Edit"
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}
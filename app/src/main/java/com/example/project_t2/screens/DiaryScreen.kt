package com.example.project_t2.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.example.project_t2.roomDB.DiaryEntity
import com.example.project_t2.roomDB.DiaryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime

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

    LaunchedEffect(Unit) {
        val todayDiary = viewModel.getDiaryByDate(LocalDate.now())
        if (todayDiary != null) {
            diaryId = todayDiary.id
            title = todayDiary.title
            content = todayDiary.content
            selectedEmotion = todayDiary.emotion
            selectedWeather = todayDiary.weather
        }

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
                            time = LocalDateTime.now()
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
                sky = sky,
                pty = pty,
                t1h = t1h
            )

            Spacer(modifier = Modifier.height(16.dp))

            EmotionSelector(
                selectedEmotion = selectedEmotion,
                onEmotionSelected = { selectedEmotion = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("오늘의 이야기를 들려주세요.") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
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
    sky: Int?,
    pty: Int?,
    t1h: Double?
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "오늘의 날씨: $weatherDescription", fontWeight = FontWeight.SemiBold)
        Text(text = "현재 기온: $t1h", fontSize = 14.sp, color = Color.DarkGray)
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
                        .clickable { onWeatherSelected(weather) },
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
        Weathers.SUNNY -> "\u2600\ufe0f"
        Weathers.CLOUDY -> "\u2601\ufe0f"
        Weathers.PARTLY_CLOUDY -> "\u26c5"
        Weathers.RAINY -> "\ud83c\udf27\ufe0f"
        Weathers.SNOWY -> "\ud83c\udf28\ufe0f"
        Weathers.STORMY -> "\u26c8\ufe0f"
        Weathers.FOGGY -> "\ud83c\udf2b\ufe0f"
        Weathers.WINDY -> "\ud83c\udf2c\ufe0f"
        Weathers.HAZY -> "\ud83e\udd76"
    }
}

@Composable
fun EmotionSelector(selectedEmotion: Emotion?, onEmotionSelected: (Emotion) -> Unit) {
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
                    .clickable { onEmotionSelected(emotion) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = emotion.emoji, fontSize = 32.sp)
            }
        }
    }
}

@Composable
fun DiaryTopAppBar(onNavigate: (String) -> Unit, onSaveClick: () -> Unit) {
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
                    text = { Text("캘린더") },
                    onClick = {
                        menuExpanded = false
                        onNavigate("calendar_search")
                    }
                )
                DropdownMenuItem(
                    text = { Text("검색") },
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
        Image(
            painter = painterResource(id = R.drawable.outline_check_circle_24),
            contentDescription = "Save",
            modifier = Modifier
                .size(50.dp)
                .clickable { onSaveClick() }
        )
    }
}
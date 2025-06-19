package com.example.project_t2.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedEmotion by remember { mutableStateOf<Emotion?>(null) }
    var selectedWeather by remember { mutableStateOf<Weathers?>(null) }
    var weatherDescription by remember { mutableStateOf("날씨 정보 로딩 중...") }

    // 수정 시 기존 다이어리의 ID를 저장하기 위한 상태
    var diaryId by remember { mutableStateOf<Int?>(null) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // 화면 진입 시 오늘 날짜의 일기가 있는지 확인
    LaunchedEffect(Unit) {
        val todayDiary = viewModel.getDiaryByDate(LocalDate.now())
        if (todayDiary != null) {
            // 오늘 쓴 일기가 있으면, 화면 상태를 해당 내용으로 채움
            diaryId = todayDiary.id
            title = todayDiary.title
            content = todayDiary.content
            selectedEmotion = todayDiary.emotion
            selectedWeather = todayDiary.weather
        }

        // 날씨 정보 가져오기
        withContext(Dispatchers.IO) {
            try {
                val items = GetWeather()
                val sky = items.find { it.category == "SKY" }?.fcstValue?.toIntOrNull()
                val rn1 = items.find { it.category == "RN1" }?.fcstValue?.toDoubleOrNull()
                val t1h = items.find { it.category == "T1H" }?.fcstValue?.toDoubleOrNull()
                val currentWeatherData = WeatherData(sky, rn1, t1h)
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
                onMenuClick = { /* TODO: 메뉴 클릭 */ },
                onSaveClick = {
                    coroutineScope.launch {
                        if (selectedWeather == null || selectedEmotion == null) {
                            Toast.makeText(context, "날씨와 감정을 선택해주세요.", Toast.LENGTH_SHORT).show()
                            return@launch
                        }

                        val diary = DiaryEntity(
                            id = diaryId ?: 0, // id가 있으면 수정, 없으면 0으로 새 일기
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
                        navController.popBackStack() // 저장 후 이전 화면으로 이동
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 제목 입력 필드
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

            // 날씨 선택 영역
            WeatherSelector(
                weatherDescription = weatherDescription,
                selectedWeather = selectedWeather,
                onWeatherSelected = { selectedWeather = it }
            )

            Spacer(modifier = Modifier.height(16.dp))


            // 감정 선택 영역
            EmotionSelector(
                selectedEmotion = selectedEmotion,
                onEmotionSelected = { selectedEmotion = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 내용 입력 필드
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("오늘의 이야기를 들려주세요.") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // 남은 공간을 모두 차지
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
fun DiaryTopAppBar(onMenuClick: () -> Unit, onSaveClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.outline_menu_24),
            contentDescription = "Menu",
            modifier = Modifier
                .size(50.dp)
                .clickable { onMenuClick() }
        )
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

@Composable
fun WeatherSelector(
    weatherDescription: String,
    selectedWeather: Weathers?,
    onWeatherSelected: (Weathers) -> Unit
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
                        .clickable { onWeatherSelected(weather) },
                    contentAlignment = Alignment.Center
                ) {
                    // TODO: 각 날씨에 맞는 아이콘으로 교체하면 더 좋습니다.
                    Text(text = weatherToEmoji(weather), fontSize = 30.sp)
                }
            }
        }
    }
}

// 임시로 날씨를 이모지로 표현하는 함수
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


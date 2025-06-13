package com.example.project_t2.screens

import android.app.Application
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
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
import com.example.project_t2.data.WeatherViewModel
import com.example.project_t2.graphics.Emotion
import com.example.project_t2.models.Weathers
import com.example.project_t2.roomDB.DiaryEntity
import com.example.project_t2.ui.theme.MainFont
import com.example.project_t2.ui.theme.Project_T2Theme
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun DiaryScreen(
    diaryId: Int,
    navController: NavController,
    diaryViewModel: DiaryViewModel = viewModel(
        factory = DiaryViewModelFactory(LocalContext.current.applicationContext as Application)
    ),
    weatherViewModel: WeatherViewModel = viewModel()
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedEmotion by remember { mutableStateOf(Emotion.SMILE) }
    val savedDiary by diaryViewModel.selectedDiary.collectAsState()
    val isLoading by diaryViewModel.isLoading.collectAsState()
    val analysisResult by diaryViewModel.analysisResult.collectAsState()
    val weatherState by weatherViewModel.weatherData.collectAsState()

    var isEditableDay by remember { mutableStateOf(false) }
    var isInEditMode by remember { mutableStateOf(false) }

    var currentWeathers by remember { mutableStateOf(Weathers.SUNNY) }
    var currentTemperature by remember { mutableStateOf("") }

    LaunchedEffect(analysisResult) {
        analysisResult?.let {
            selectedEmotion = it
            diaryViewModel.onAnalysisComplete()
        }
    }

    LaunchedEffect(content) {
        if (isInEditMode && content.isNotBlank()) {
            delay(1000L)
            diaryViewModel.analyzeEmotion(content)
        }
    }

    LaunchedEffect(key1 = diaryId) {
        if (diaryId != -1) {
            diaryViewModel.getDiary(diaryId)
        } else {
            isEditableDay = true
            isInEditMode = true
        }
        val current = LocalDateTime.now()
        val baseTime = if (current.minute < 40) current.minusHours(1).format(DateTimeFormatter.ofPattern("HH00")) else current.format(DateTimeFormatter.ofPattern("HH00"))
        val baseDate = current.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        weatherViewModel.loadWeather(baseDate = baseDate, baseTime = baseTime, nx = 60, ny = 127)
    }

    LaunchedEffect(weatherState) {
        weatherState?.response?.body?.items?.item?.let { items ->
            val skyValue = items.find { it.category == "SKY" }?.fcstValue
            val tempValue = items.find { it.category == "T1H" }?.fcstValue
            val rainValueStr = items.find { it.category == "RN1" }?.fcstValue
            currentTemperature = tempValue?.let { "$it°C" } ?: ""
            val rainAmount = rainValueStr?.replace(Regex("[^0-9.]"), "")?.toDoubleOrNull() ?: 0.0
            currentWeathers = when {
                rainAmount > 0.0 -> Weathers.RAINY
                skyValue == "1" -> Weathers.SUNNY
                skyValue == "3" -> Weathers.PARTLY_CLOUDY
                skyValue == "4" -> Weathers.CLOUDY
                else -> Weathers.SUNNY
            }
        }
    }

    LaunchedEffect(key1 = savedDiary) {
        savedDiary?.let {
            title = it.title
            content = it.content
            selectedEmotion = it.emotion
        }
        if (diaryId != -1) {
            val diaryDate = savedDiary?.time?.toLocalDate()
            isEditableDay = (diaryDate == LocalDate.now())
            isInEditMode = false
        }
    }

    DiaryScreenContent(
        navController = navController,
        title = title,
        content = content,
        selectedEmotion = selectedEmotion,
        currentWeathers = currentWeathers,
        currentTemperature = currentTemperature,
        isEditableDay = isEditableDay,
        isInEditMode = isInEditMode,
        isLoading = isLoading,
        onTitleChange = { title = it },
        onContentChange = { content = it },
        onEmotionChange = { selectedEmotion = it },
        onSaveClick = {
            val diary = if (diaryId == -1) {
                DiaryEntity(title = title, weather = currentWeathers, emotion = selectedEmotion, content = content)
            } else {
                DiaryEntity(id = diaryId, title = title, weather = savedDiary?.weather ?: currentWeathers, emotion = selectedEmotion, content = content, time = savedDiary?.time ?: LocalDateTime.now())
            }
            if (diaryId == -1) diaryViewModel.insertDiary(diary) else diaryViewModel.updateDiary(diary)
            isInEditMode = false
        },
        onEditClick = { isInEditMode = true }
    )
}

@Composable
private fun DiaryScreenContent(
    navController: NavController,
    title: String,
    content: String,
    selectedEmotion: Emotion,
    currentWeathers: Weathers,
    currentTemperature: String,
    isEditableDay: Boolean,
    isInEditMode: Boolean,
    isLoading: Boolean,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onEmotionChange: (Emotion) -> Unit,
    onSaveClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.paper_texture), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())

        Column(modifier = Modifier.fillMaxSize()) {
            // 상단 바
            Row(
                modifier = Modifier.fillMaxWidth().height(56.dp).padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MenuDropdown(navController = navController)
                if (isEditableDay) {
                    if (isInEditMode) {
                        Image(painter = painterResource(id = R.drawable.outline_check_circle_24), contentDescription = "Save", modifier = Modifier.size(50.dp).clickable(onClick = onSaveClick))
                    } else {
                        IconButton(onClick = onEditClick) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(36.dp))
                        }
                    }
                }
            }

            // [수정] 날씨와 감정 선택 UI를 하나의 Card 안에 배치
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(text = "오늘의 날씨: ${currentWeathers.name}  ${currentTemperature}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    }
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Emotion.entries.forEach { emotion ->
                            val isSelected = emotion == selectedEmotion
                            val scale by animateFloatAsState(targetValue = if (isSelected) 1.2f else 1.0f, label = "")
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent)
                                    .clickable(enabled = isInEditMode) { onEmotionChange(emotion) }
                            ) {
                                Text(
                                    text = emotion.emoji,
                                    fontSize = 32.sp,
                                    modifier = Modifier.scale(scale).padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 일기 작성 영역
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                item {
                    // 제목 입력
                    Column {
                        BasicTextField(
                            value = title,
                            onValueChange = onTitleChange,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontSize = 24.sp, fontFamily = MainFont),
                            cursorBrush = SolidColor(Color.Black),
                            readOnly = !isInEditMode,
                            decorationBox = { innerTextField ->
                                if (title.isEmpty()) {
                                    Text("제목을 입력하세요", style = TextStyle(color = Color.Gray, fontSize = 24.sp, fontFamily = MainFont))
                                }
                                innerTextField()
                            }
                        )
                        Divider(modifier = Modifier.padding(top = 8.dp), color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 내용 입력
                    BasicTextField(
                        value = content,
                        onValueChange = onContentChange,
                        modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 400.dp),
                        textStyle = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontFamily = FontFamily.Default),
                        cursorBrush = SolidColor(Color.Black),
                        readOnly = !isInEditMode,
                        decorationBox = { innerTextField ->
                            if (content.isEmpty()) {
                                Text("오늘 하루는 어땠나요?", style = TextStyle(color = Color.Gray, fontSize = 16.sp, fontFamily = FontFamily.Default))
                            }
                            innerTextField()
                        }
                    )
                }
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiaryScreenPreview() {
    Project_T2Theme {
        DiaryScreenContent(
            navController = rememberNavController(),
            title = "오늘의 일기 제목",
            content = "일기 내용",
            selectedEmotion = Emotion.SMILE,
            currentWeathers = Weathers.SUNNY,
            currentTemperature = "25°C",
            isEditableDay = true,
            isInEditMode = true,
            isLoading = false,
            onTitleChange = {},
            onContentChange = {},
            onEmotionChange = {},
            onSaveClick = {},
            onEditClick = {}
        )
    }
}
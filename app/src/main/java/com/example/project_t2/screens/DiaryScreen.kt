package com.example.project_t2.screens

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
    weatherViewModel: WeatherViewModel = viewModel(),
    openDrawer: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedEmotion by remember { mutableStateOf(Emotion.SMILE) }
    val savedDiary by diaryViewModel.selectedDiary.collectAsState()
    val weatherState by weatherViewModel.weatherData.collectAsState()

    var isEditableDay by remember { mutableStateOf(false) }
    var isInEditMode by remember { mutableStateOf(false) }

    var currentWeathers by remember { mutableStateOf(Weathers.SUNNY) }
    var currentTemperature by remember { mutableStateOf("") }

    LaunchedEffect(weatherState) {
        weatherState?.response?.body?.items?.item?.let { items ->
            val skyValue = items.find { it.category == "SKY" }?.fcstValue
            val tempValue = items.find { it.category == "T1H" }?.fcstValue
            val rainValue = items.find { it.category == "RN1" }?.fcstValue

            currentTemperature = tempValue?.let { "$it°C" } ?: ""

            currentWeathers = when {
                rainValue != "0.0mm" && rainValue != null -> Weathers.RAINY
                skyValue == "1" -> Weathers.SUNNY
                skyValue == "3" -> Weathers.PARTLY_CLOUDY
                skyValue == "4" -> Weathers.CLOUDY
                else -> Weathers.SUNNY
            }
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
        val baseTime = if (current.minute < 40) {
            current.minusHours(1).format(DateTimeFormatter.ofPattern("HH00"))
        } else {
            current.format(DateTimeFormatter.ofPattern("HH00"))
        }
        val baseDate = current.format(DateTimeFormatter.ofPattern("yyyyMMdd"))

        weatherViewModel.loadWeather(
            baseDate = baseDate,
            baseTime = baseTime,
            nx = 60,
            ny = 127
        )
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

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.paper_texture),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.outline_menu_24),
                    contentDescription = "Menu",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable { openDrawer() }
                )

                if (isEditableDay) {
                    if (isInEditMode) {
                        Image(
                            painter = painterResource(id = R.drawable.outline_check_circle_24),
                            contentDescription = "Save",
                            modifier = Modifier
                                .size(50.dp)
                                .clickable {
                                    val diary = if (diaryId == -1) {
                                        DiaryEntity(
                                            title = title,
                                            weather = currentWeathers,
                                            emotion = selectedEmotion,
                                            content = content
                                        )
                                    } else {
                                        DiaryEntity(
                                            id = diaryId,
                                            title = title,
                                            weather = savedDiary?.weather ?: currentWeathers,
                                            emotion = selectedEmotion,
                                            content = content,
                                            time = savedDiary?.time ?: LocalDateTime.now()
                                        )
                                    }

                                    if (diaryId == -1) {
                                        diaryViewModel.insertDiary(diary)
                                    } else {
                                        diaryViewModel.updateDiary(diary)
                                    }

                                    isInEditMode = false
                                }
                        )
                    } else {
                        IconButton(onClick = { isInEditMode = true }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "오늘의 날씨: ${currentWeathers.name}  ${currentTemperature}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                item {
                    BasicTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                        cursorBrush = SolidColor(Color.Black),
                        readOnly = !isInEditMode,
                        decorationBox = { innerTextField ->
                            if (title.isEmpty()) {
                                Text(
                                    "제목",
                                    style = TextStyle(
                                        color = Color.Gray,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            innerTextField()
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    BasicTextField(
                        value = content,
                        onValueChange = { content = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 400.dp),
                        textStyle = TextStyle(fontSize = 16.sp, lineHeight = 24.sp),
                        cursorBrush = SolidColor(Color.Black),
                        readOnly = !isInEditMode,
                        decorationBox = { innerTextField ->
                            if (content.isEmpty()) {
                                Text(
                                    "오늘 하루는 어땠나요?",
                                    style = TextStyle(color = Color.Gray, fontSize = 16.sp)
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }
        }
    }
}
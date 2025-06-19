package com.example.project_t2.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.project_t2.graphics.Emotion
import com.example.project_t2.models.AppBackground
import com.example.project_t2.models.GenericTopAppBar
import com.example.project_t2.models.Sentiments
import com.example.project_t2.models.Statistics.Period
import com.example.project_t2.ui.theme.MainFont
import com.example.project_t2.viewmodel.stats.StatViewModel

private data class PredictionUiModel(
    val imageResId: Int?,
    val message: String,
    val emojiForNone: String? = null
)

@Composable
private fun mapSentimentToUiModel(sentiment: Sentiments): PredictionUiModel {
    return when (sentiment) {
        Sentiments.HAPPY -> PredictionUiModel(Emotion.HAPPY.imageResId, "오늘은 '행복'한 하루가 될 것 같아요!")
        Sentiments.TENDER -> PredictionUiModel(Emotion.TENDER.imageResId, "오늘은 '평온'한 하루가 될 것 같아요!")
        Sentiments.SAD -> PredictionUiModel(Emotion.SAD.imageResId, "오늘은 '슬픈' 감정이 들 수 있어요. 힘내세요!")
        Sentiments.ANGRY, Sentiments.FEAR -> PredictionUiModel(Emotion.BAD.imageResId, "오늘은 좋지 않은 일이 생길 수 있으니 마음을 다스려보세요.")
        Sentiments.NONE -> PredictionUiModel(null, "데이터가 부족하여 예측할 수 없어요.", "🤔")
    }
}


@Composable
fun StatScreen(navController: NavController, viewModel: StatViewModel) {
    val selectedPeriod by viewModel.selectedPeriod.collectAsState()
    val emotionStats by viewModel.emotionStats.collectAsState()
    val predictedSentiment by viewModel.predictedSentiment.collectAsState()
    val isPredicting by viewModel.isPredicting.collectAsState()

    AppBackground {
        Scaffold(
            topBar = {
                GenericTopAppBar(title = "통계 및 예측", onNavigate = { route -> navController.navigate(route) })
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PeriodSelector(
                    selectedPeriod = selectedPeriod,
                    onPeriodSelected = { viewModel.setPeriod(it) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (emotionStats.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("해당 기간에 작성된 일기가 없습니다.", style = MaterialTheme.typography.bodyLarge)
                    }
                } else {
                    EmotionBarChart(stats = emotionStats)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp), color = Color.Gray.copy(alpha = 0.5f))

                SentimentPrediction(
                    isPredicting = isPredicting,
                    predictedSentiment = predictedSentiment,
                    onPredictClick = { viewModel.predictTodaysSentiment() }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 추가된 설명 문구
                Text(
                    text = "※ 감정 예측은 과거의 날씨와 그날 기록된 감정 데이터를 기반으로 합니다.\n실제 감정과 다를 수 있으니 재미로 확인해주세요.",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun SentimentPrediction(
    isPredicting: Boolean,
    predictedSentiment: Sentiments?,
    onPredictClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("오늘의 감정 예측", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onPredictClick,
            enabled = !isPredicting,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("예측 시작!", style = MaterialTheme.typography.labelLarge)
        }
        Spacer(modifier = Modifier.height(24.dp))

        if (isPredicting) {
            CircularProgressIndicator()
        } else {
            if (predictedSentiment != null) {
                val uiModel = mapSentimentToUiModel(predictedSentiment)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (uiModel.imageResId != null) {
                        Image(
                            painter = painterResource(id = uiModel.imageResId),
                            contentDescription = "Predicted Emotion",
                            modifier = Modifier.size(80.dp)
                        )
                    } else {
                        Text(uiModel.emojiForNone ?: "", fontSize = 64.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiModel.message,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Text(
                    text = "버튼을 눌러 오늘의 감정을 예측해보세요.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun PeriodSelector(selectedPeriod: Period, onPeriodSelected: (Period) -> Unit) {
    val periods = Period.values()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        periods.forEach { period ->
            val isSelected = period == selectedPeriod
            val containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            val contentColor = MaterialTheme.colorScheme.onPrimary

            Button(
                onClick = { onPeriodSelected(period) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = containerColor,
                    contentColor = contentColor
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(period.toDisplayName(), style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

fun Period.toDisplayName(): String {
    return when (this) {
        Period.WEEK -> "1주"
        Period.MONTH -> "1개월"
        Period.HALF_YEAR -> "6개월"
        Period.YEAR -> "1년"
        Period.ALL -> "전체"
    }
}

@Composable
fun EmotionBarChart(stats: Map<Emotion, Float>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("감정 통계", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        stats.entries.sortedByDescending { it.value }.forEach { (emotion, percentage) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = emotion.imageResId),
                    contentDescription = emotion.displayName,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = emotion.displayName,
                    modifier = Modifier.width(80.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(percentage)
                            .height(24.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(emotionToColor(emotion))
                    )
                }
                Text(
                    text = "${(percentage * 100).toInt()}%",
                    modifier = Modifier.width(50.dp),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun emotionToColor(emotion: Emotion): Color {
    return when (emotion) {
        Emotion.HAPPY -> Color(0xFFFBC02D) // Yellow
        Emotion.JOY -> Color(0xFF66BB6A)   // Green
        Emotion.TENDER -> Color(0xFFB0BEC5) // Blue Grey
        Emotion.SAD -> Color(0xFF42A5F5)   // Blue
        Emotion.BAD -> Color(0xFFEF5350)   // Red
    }
}
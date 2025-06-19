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
import com.example.project_t2.models.GenericTopAppBar
import com.example.project_t2.models.Sentiments
import com.example.project_t2.models.Statistics.Period
import com.example.project_t2.viewmodel.stats.StatViewModel

@Composable
fun StatScreen(navController: NavController, viewModel: StatViewModel) {
    val selectedPeriod by viewModel.selectedPeriod.collectAsState()
    val emotionStats by viewModel.emotionStats.collectAsState()
    val predictedSentiment by viewModel.predictedSentiment.collectAsState()
    val isPredicting by viewModel.isPredicting.collectAsState()

    Scaffold(
        topBar = {
            GenericTopAppBar(title = "통계 및 예측", onNavigate = { route -> navController.navigate(route) })
        }
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
                    Text("해당 기간에 작성된 일기가 없습니다.")
                }
            } else {
                EmotionBarChart(stats = emotionStats)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

            SentimentPrediction(
                isPredicting = isPredicting,
                predictedSentiment = predictedSentiment,
                onPredictClick = { viewModel.predictTodaysSentiment() }
            )
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
        Text("오늘의 감정 예측", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onPredictClick,
            enabled = !isPredicting,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("예측 시작!")
        }
        Spacer(modifier = Modifier.height(24.dp))

        if (isPredicting) {
            CircularProgressIndicator()
        } else {
            if (predictedSentiment != null) {
                val (emoji, text, message) = sentimentToUiElements(predictedSentiment)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(emoji, fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(message, style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                Text("버튼을 눌러 오늘의 감정을 예측해보세요.")
            }
        }
    }
}

fun sentimentToUiElements(sentiment: Sentiments): Triple<String, String, String> {
    return when (sentiment) {
        Sentiments.HAPPY -> Triple("😄", "행복", "오늘은 '행복'한 하루가 될 것 같아요!")
        Sentiments.TENDER -> Triple("😌", "평온", "오늘은 '평온'한 하루가 될 것 같아요!")
        Sentiments.SAD -> Triple("😢", "슬픔", "오늘은 '슬픈' 감정이 들 수 있어요. 힘내세요!")
        Sentiments.ANGRY -> Triple("😠", "화남", "오늘은 '화나는' 일이 생길 수 있으니 마음을 다스려보세요.")
        Sentiments.FEAR -> Triple("😱", "두려움", "오늘은 '두려운' 일이 생길 수 있지만, 잘 해낼 수 있어요.")
        Sentiments.NONE -> Triple("🤔", "알 수 없음", "데이터가 부족하여 예측할 수 없어요.")
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
            val containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

            Button(
                onClick = { onPeriodSelected(period) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = containerColor,
                    contentColor = contentColor
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(period.toDisplayName())
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
        Text("감정 통계", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        stats.entries.sortedByDescending { it.value }.forEach { (emotion, percentage) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // 수정된 부분: Image와 Text로 감정 표시
                Image(
                    painter = painterResource(id = emotion.imageResId),
                    contentDescription = emotion.displayName,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = emotion.displayName,
                    modifier = Modifier.width(80.dp),
                    fontSize = 16.sp
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
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun emotionToColor(emotion: Emotion): Color {
    // 수정된 부분: 5가지 감정에 대한 색상 매핑
    return when (emotion) {
        Emotion.HAPPY -> Color(0xFFFFC107)
        Emotion.JOY -> Color(0xFF81C784)
        Emotion.TENDER-> Color(0xFFB0BEC5)
        Emotion.SAD -> Color(0xFF64B5F6)
        Emotion.BAD -> Color(0xFFE57373)
    }
}
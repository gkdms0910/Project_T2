package com.example.project_t2.screens

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_t2.R
import com.example.project_t2.data.DiaryViewModel
import com.example.project_t2.data.DiaryViewModelFactory
import com.example.project_t2.graphics.RenderImage
import com.example.project_t2.graphics.cloudImage
import com.example.project_t2.ui.theme.MainFont
import com.example.project_t2.ui.theme.Project_T2Theme

/**
 * 로직과 상태를 관리하는 Stateful 컴포저블
 */
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    diaryViewModel: DiaryViewModel = viewModel(
        factory = DiaryViewModelFactory(LocalContext.current.applicationContext as Application)
    )
) {
    val navigateToDiaryId by diaryViewModel.navigateToDiary.collectAsState()

    LaunchedEffect(navigateToDiaryId) {
        navigateToDiaryId?.let { id ->
            navController.navigate("diary/$id")
            diaryViewModel.onNavigationComplete()
        }
    }

    MainScreenContent(
        modifier = modifier,
        onClick = { diaryViewModel.findDiaryForToday() }
    )
}

/**
 * UI만 담당하는 Stateless 컴포저블
 */
@Composable
private fun MainScreenContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val cloud = cloudImage()

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
    ) {
        // 배경 이미지
        Image(
            painter = painterResource(id = R.drawable.paper_texture),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 콘텐츠
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xD37CCCF4),
                            Color(0xA9FFFFFF)
                        )
                    )
                ),
            contentAlignment = Alignment.Center // [수정] 중앙 정렬
        ) {
            // 구름 이미지들을 배경으로 배치
            RenderImage(
                cloud, modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .align(Alignment.TopEnd)
                    .offset(x = (-20).dp, y = 300.dp)
            )
            RenderImage(
                cloud, modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .align(Alignment.BottomStart)
                    .offset(x = 20.dp, y = (-200).dp)
            )

            // [수정] 환영 메시지 텍스트
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "대체로 맑음",
                    fontFamily = MainFont, // 컨셉 폰트 적용
                    fontSize = 42.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 50.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "화면을 터치하여 일기를 시작하세요",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

/**
 * 간단해진 프리뷰
 */
@Preview(showBackground = true)
@Composable
private fun PreviewMainScreen() {
    Project_T2Theme {
        MainScreenContent(onClick = {})
    }
}
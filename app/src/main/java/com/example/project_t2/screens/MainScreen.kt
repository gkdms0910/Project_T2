package com.example.project_t2.screens

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_t2.R
import com.example.project_t2.data.DiaryViewModel
import com.example.project_t2.data.DiaryViewModelFactory
import com.example.project_t2.graphics.RenderImage
import com.example.project_t2.graphics.cloudImage
import com.example.project_t2.graphics.titleText
import com.example.project_t2.ui.theme.Project_T2Theme

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

@Composable
private fun MainScreenContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val cloud = cloudImage()
    val title = titleText()

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = R.drawable.paper_texture),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
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
                )
        ) {
            RenderImage(
                title, modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.TopCenter)
                    .padding(top = 200.dp)
            )
            RenderImage(
                cloud, modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .offset(x = 10.dp, y = 300.dp)
            )
            RenderImage(
                cloud, modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .offset(x = 180.dp, y = 500.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMainScreen() {
    Project_T2Theme {
        MainScreenContent(onClick = {})
    }
}
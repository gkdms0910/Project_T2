package com.example.project_t2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.project_t2.R
import com.example.project_t2.models.BackgroundViewModel
import com.example.project_t2.models.GenericTopAppBar
import com.example.project_t2.ui.theme.AppFontSize
import com.example.project_t2.ui.theme.AppTheme
import com.example.project_t2.ui.theme.MainFont
import com.example.project_t2.viewmodel.ThemeViewModel

@Composable
fun SettingScreen(
    navController: NavController,
    backgroundViewModel: BackgroundViewModel,
    themeViewModel: ThemeViewModel,
    currentFontSize: AppFontSize,
    onFontSizeChange: (AppFontSize) -> Unit
) {
    Scaffold(
        topBar = {
            GenericTopAppBar(
                title = "설정",
                onNavigate = { route -> navController.navigate(route) }
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FontSizeSettingBox(
                currentFontSize = currentFontSize,
                onFontSizeChange = onFontSizeChange
            )
            BackgroundSettingBox(
                viewModel = backgroundViewModel
            )
            ThemeSettingBox(
                viewModel = themeViewModel
            )
        }
    }
}

@Composable
fun FontSizeSettingBox(
    currentFontSize: AppFontSize,
    onFontSizeChange: (AppFontSize) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Text(text = "글자 크기 설정", fontSize = 18.sp, fontFamily = MainFont)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = { onFontSizeChange(AppFontSize.LARGE) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentFontSize == AppFontSize.LARGE) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("크게", fontSize = 16.sp, fontFamily = MainFont)
            }
            Button(
                onClick = { onFontSizeChange(AppFontSize.MEDIUM) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentFontSize == AppFontSize.MEDIUM) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("중간", fontSize = 16.sp, fontFamily = MainFont)
            }
            Button(
                onClick = { onFontSizeChange(AppFontSize.SMALL) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentFontSize == AppFontSize.SMALL) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("작게", fontSize = 16.sp, fontFamily = MainFont)
            }
        }
    }
}

@Composable
fun BackgroundSettingBox(viewModel: BackgroundViewModel) {
    val currentBg by viewModel.backgroundResId.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Text(text = "배경화면 변경", fontSize = 18.sp, fontFamily = MainFont)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = { viewModel.setBackground(R.drawable.paper_texture) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentBg == R.drawable.paper_texture) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("기본 질감", fontFamily = MainFont)
            }
            Button(
                onClick = { viewModel.setBackground(R.drawable.white) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentBg == R.drawable.white) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("흰색 배경", fontFamily = MainFont)
            }
        }
    }
}

@Composable
fun ThemeSettingBox(viewModel: ThemeViewModel) {
    val currentTheme by viewModel.theme.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Text(text = "테마 색상 변경", fontSize = 18.sp, fontFamily = MainFont)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = { viewModel.setTheme(AppTheme.DEFAULT) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentTheme == AppTheme.DEFAULT) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("기본", fontFamily = MainFont)
            }
            Button(
                onClick = { viewModel.setTheme(AppTheme.FOREST) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentTheme == AppTheme.FOREST) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("숲", fontFamily = MainFont)
            }
            Button(
                onClick = { viewModel.setTheme(AppTheme.SKY) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentTheme == AppTheme.SKY) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("하늘", fontFamily = MainFont)
            }
        }
    }
}
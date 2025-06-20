package com.example.project_t2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.rememberNavController
import com.example.project_t2.models.BackgroundViewModel
import com.example.project_t2.navigation.AppNavGraph
import com.example.project_t2.ui.theme.AppFontSize
import com.example.project_t2.ui.theme.Project_T2Theme
import com.example.project_t2.viewmodel.ThemeViewModel

class MainActivity : ComponentActivity() {

    private val backgroundViewModel: BackgroundViewModel by viewModels()
    private val themeViewModel: ThemeViewModel by viewModels() // ThemeViewModel 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val backgroundResId by backgroundViewModel.backgroundResId.collectAsState()
            val colorScheme by themeViewModel.colorScheme.collectAsState() // 테마 상태 구독
            val fontSize = remember { mutableStateOf(AppFontSize.MEDIUM) }

            Project_T2Theme(
                colorScheme = colorScheme, // ViewModel의 상태를 테마에 전달
                fontSize = fontSize.value
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = backgroundResId),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    val navController = rememberNavController()
                    AppNavGraph(
                        navController = navController,
                        backgroundViewModel = backgroundViewModel,
                        themeViewModel = themeViewModel,
                        currentFontSize = fontSize.value,
                        onFontSizeChange = { fontSize.value = it }
                    )
                }
            }
        }
    }
}
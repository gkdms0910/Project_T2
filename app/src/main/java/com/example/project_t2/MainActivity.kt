package com.example.project_t2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.Project_T2.ui.WeatherScreen
import com.example.project_t2.navigation.AppNavGraph
import com.example.project_t2.ui.theme.Project_T2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Project_T2Theme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    floatingActionButton = {
                        // 메인 화면과 일기 목록 화면에서만 FAB 표시
                        if (currentRoute == "main" || currentRoute == "diaryList") {
                            FloatingActionButton(
                                onClick = {
                                    // 새 일기 작성 화면으로 이동
                                    navController.navigate("diary/-1")
                                }
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = "새 일기 작성")
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavGraph(navController = navController)
                    }
                }
            }
        }
    }
}
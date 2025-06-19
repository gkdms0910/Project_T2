package com.example.project_t2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.example.project_t2.navigation.AppNavGraph
import com.example.project_t2.ui.theme.AppFontSize
import com.example.project_t2.ui.theme.Project_T2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val fontSize = remember { mutableStateOf(AppFontSize.MEDIUM) }
            Project_T2Theme(fontSize = fontSize.value) {
                val navController = rememberNavController()
                AppNavGraph(
                    navController = navController,
                    currentFontSize = fontSize.value,
                    onFontSizeChange = { fontSize.value = it }
                )
            }
        }
    }
}
package com.example.project_t2.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.project_t2.models.GenericTopAppBar

@Composable
fun StatScreen(navController: NavController) {
    Scaffold(
        topBar = {
            GenericTopAppBar(title = "통계", onNavigate = { route -> navController.navigate(route) })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("통계 화면")
        }
    }
}
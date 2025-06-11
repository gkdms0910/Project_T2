package com.example.project_t2.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AppDrawer(
    navController: NavController,
    closeDrawer: () -> Unit
) {
    ModalDrawerSheet {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("메뉴", style = MaterialTheme.typography.titleLarge)
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            NavigationDrawerItem(
                label = { Text("일기 목록 보기") },
                icon = { Icon(Icons.Default.List, contentDescription = "일기 목록") },
                selected = false,
                onClick = {
                    navController.navigate("diaryList")
                    closeDrawer()
                }
            )
        }
    }
}
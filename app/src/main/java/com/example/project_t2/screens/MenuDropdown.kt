package com.example.project_t2.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project_t2.R

@Composable
fun MenuDropdown(navController: NavController) {
    var menuExpanded by remember { mutableStateOf(false) }

    Box {
        Image(
            painter = painterResource(id = R.drawable.outline_menu_24),
            contentDescription = "Menu",
            modifier = Modifier
                .size(50.dp)
                .clickable { menuExpanded = true }
        )
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("메인 화면으로") },
                onClick = {
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }
                    menuExpanded = false
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Home, contentDescription = "메인 화면")
                }
            )
            Divider()
            DropdownMenuItem(
                text = { Text("일기 목록 보기") },
                onClick = {
                    navController.navigate("diaryList")
                    menuExpanded = false
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.List, contentDescription = "일기 목록")
                }
            )
        }
    }
}
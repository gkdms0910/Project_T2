package com.example.project_t2.models

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.project_t2.R
import com.example.project_t2.ui.theme.MainFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericTopAppBar(title: String, onNavigate: (String) -> Unit) {
    var menuExpanded by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = { Text(text = title, fontFamily = MainFont) },
        navigationIcon = {
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Image(
                        painter = painterResource(id = R.drawable.outline_menu_24),
                        contentDescription = "Menu"
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("일기 작성", fontFamily = MainFont) },
                        onClick = {
                            menuExpanded = false
                            onNavigate("diary")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("캘린더/검색", fontFamily = MainFont) },
                        onClick = {
                            menuExpanded = false
                            onNavigate("calendar_search")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("통계", fontFamily = MainFont) },
                        onClick = {
                            menuExpanded = false
                            onNavigate("stats")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("설정", fontFamily = MainFont) },
                        onClick = {
                            menuExpanded = false
                            onNavigate("settings")
                        }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}
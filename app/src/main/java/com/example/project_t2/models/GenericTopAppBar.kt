package com.example.project_t2.models

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_t2.R

@Composable
fun GenericTopAppBar(title: String, onNavigate: (String) -> Unit) {
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left part: Menu
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
                    text = { Text("일기 작성") },
                    onClick = {
                        menuExpanded = false
                        onNavigate("diary")
                    }
                )
                DropdownMenuItem(
                    text = { Text("캘린더/검색") },
                    onClick = {
                        menuExpanded = false
                        onNavigate("calendar_search")
                    }
                )
                DropdownMenuItem(
                    text = { Text("통계") },
                    onClick = {
                        menuExpanded = false
                        onNavigate("stats")
                    }
                )
                DropdownMenuItem(
                    text = { Text("설정") },
                    onClick = {
                        menuExpanded = false
                        onNavigate("settings")
                    }
                )
            }
        }

        // Center part: Title
        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)

        // Right part: Spacer to balance the layout
        Spacer(modifier = Modifier.size(50.dp))
    }
}
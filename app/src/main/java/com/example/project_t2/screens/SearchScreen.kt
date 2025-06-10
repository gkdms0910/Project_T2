package com.example.project_t2.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.project_t2.R


@Composable
fun SearchScreen() {
    Box(modifier = Modifier) {
        // 배경 이미지
        Image(
            painter = painterResource(id = R.drawable.paper_texture),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.background(Color.White)
                .padding(16.dp)
        ) {
            // Top App Bar - Hamburger Menu Placeholder
            Box(
                modifier = Modifier
            ) {
                Image(
                    painter = painterResource(id = com.example.project_t2.R.drawable.outline_menu_24),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp)
                        .size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(24.dp))
                    .padding(horizontal = 16.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Search...", color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Content Box (e.g., Calendar or Results)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.LightGray)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSearchScreen() {
    SearchScreen()
}
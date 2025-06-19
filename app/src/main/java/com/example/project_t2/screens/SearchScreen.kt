package com.example.project_t2.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_t2.R
import com.example.project_t2.models.GenericTopAppBar


@Composable
fun SearchScreen(navController: NavController) {
    Scaffold(
        topBar = {
            GenericTopAppBar(title = "검색", onNavigate = { route -> navController.navigate(route) })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
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
                    .padding(16.dp)
            ) {
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
}

@Preview
@Composable
private fun PreviewSearchScreen() {
    SearchScreen(navController = rememberNavController())
}
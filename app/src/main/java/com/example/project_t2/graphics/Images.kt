package com.example.project_t2.graphics

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.project_t2.R

@Composable
fun cloudImage(): ImageSpec {
    return ImageSpec(
        painter = painterResource(id = R.drawable.cloud),
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
    )
}

@Composable
fun sunImage(): ImageSpec {
    return ImageSpec(
        painter = painterResource(id = R.drawable.sun),
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
    )
}

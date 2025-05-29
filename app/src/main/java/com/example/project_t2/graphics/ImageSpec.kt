package com.example.project_t2.graphics

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

data class ImageSpec(
    val painter: Painter,
    val contentScale: ContentScale = ContentScale.Crop,
    val alpha: Float = 1f,
    val modifier: Modifier
)

@Composable
fun RenderImage(imageSpec: ImageSpec, modifier: Modifier = Modifier) {
    Image(
        painter = imageSpec.painter,
        contentDescription = null,
        contentScale = imageSpec.contentScale,
        alpha = imageSpec.alpha,
        modifier = modifier.then(imageSpec.modifier)
    )
}
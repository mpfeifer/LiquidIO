package com.example.liquidio.components

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun LiquidIOImage(id: Int, modifier: Modifier, clazz: Class<*>) {
    val paddingValue = 32
    val context = LocalContext.current
    Image(
        painter = painterResource(id = id),
        contentDescription = "Liquid Input",
        modifier = modifier
            .padding(16.dp) // Space between the images and screen edges
            .fillMaxSize()
            .clip(RoundedCornerShape(24.dp)) // Rounds the image area
            .background(Color.LightGray.copy(alpha = 0.3f)) // Background for this image
            .padding(paddingValue.dp) // Padding inside the rounded box
            .clickable {
                val intent = Intent(context, clazz)
                context.startActivity(intent)
            }
    )
}
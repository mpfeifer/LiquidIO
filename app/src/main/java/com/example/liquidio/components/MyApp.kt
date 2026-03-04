package com.example.liquidio.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.example.liquidio.R
import com.example.liquidio.activities.FormLiquidInput
import com.example.liquidio.activities.FormLiquidOutput
import com.example.liquidio.ui.theme.LiquidIOTheme

@Composable
fun Images (modifier: Modifier) {
    LiquidIOImage(R.drawable.baby_bottle_svgrepo_com, modifier = modifier, FormLiquidInput::class.java)
    LiquidIOImage(id = R.drawable.diaper_svgrepo_com, modifier = modifier, FormLiquidOutput::class.java)
}

@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(modifier = modifier.fillMaxSize()) { Images(Modifier.weight(1.0f)) }
    } else {
        Column(modifier = modifier.fillMaxSize()) { Images(Modifier.weight(1.0f)) }
    }
}

@Preview(showBackground = true)
@Composable
fun MyAppPreview() {
    LiquidIOTheme {
        MyApp(Modifier.fillMaxSize())
    }
}
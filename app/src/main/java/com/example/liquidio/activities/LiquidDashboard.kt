package com.example.liquidio.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.liquidio.components.DashboardScreen
import com.example.liquidio.data.AppDatabase
import com.example.liquidio.ui.theme.LiquidIOTheme

class LiquidDashboard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(this)

        setContent {
            LiquidIOTheme {
                // Collect the Flow from Room as a Compose State
                val records by db.liquidDao().getAllRecords().collectAsState(initial = emptyList())

                DashboardScreen(records = records)
            }
        }
    }
}
package com.example.liquidio.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.liquidio.activities.LiquidDashboard
import com.example.liquidio.activities.MainActivity
import com.example.liquidio.data.AppDatabase
import com.example.liquidio.ui.theme.LiquidIOTheme

import com.example.liquidio.data.LiquidRecord
import kotlinx.coroutines.launch

@Composable
fun RegisterLiquid(title: String, flavor: String) {
    var mlValue by remember { mutableIntStateOf(0) }
    val flavor: String = flavor
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LiquidIOTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
            // Outer Column to hold the Form and the Bottom Bar
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
            ) {

                // 1. MAIN CONTENT (Weight = 1.0f)
                // This fills all available space, pushing the Row below it to the bottom
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "$mlValue ml",
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    RotationKnob { newValue ->
                        mlValue = newValue
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Rotate the knob to adjust",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                // 2. BOTTOM ICON ROW
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 96.dp, start = 24.dp, end = 24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val context = LocalContext.current

                    IconButton(onClick = {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancel",
                            tint = Color.Red
                        )
                    }

                    IconButton(onClick = {
                        val intent = Intent(context, LiquidDashboard::class.java)
                        context.startActivity(intent)
                    }) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = "Settings")
                    }

                    IconButton(onClick = {
                        scope.launch {
                            if (mlValue > 0) {
                                var value: Int = mlValue
                                if (flavor == "OUTPUT")
                                    value *= -1
                                val record = LiquidRecord(amount = value, type = flavor)

                                val db = AppDatabase.getDatabase(context)
                                db.liquidDao().insertRecord(record)

                                snackbarHostState.showSnackbar("Added $mlValue ml")
                            }
                            else {
                                snackbarHostState.showSnackbar("Please enter a value")
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Confirm",
                            tint = Color.Green
                        )
                    }
                }
            }
        }
    }
}

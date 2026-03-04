package com.example.liquidio.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.liquidio.activities.MainActivity
import com.example.liquidio.data.AppDatabase
import com.example.liquidio.data.LiquidRecord
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardScreen(records: List<LiquidRecord>) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = AppDatabase.getDatabase(context)

    // Calculate the sum for "Today"
    val calendar = Calendar.getInstance()
    val todayDay = calendar.get(Calendar.DAY_OF_YEAR)
    val todayYear = calendar.get(Calendar.YEAR)

    val todaysTotal = records.filter {
        val recordCal = Calendar.getInstance().apply { timeInMillis = it.timestamp }
        recordCal.get(Calendar.DAY_OF_YEAR) == todayDay &&
                recordCal.get(Calendar.YEAR) == todayYear
    }.sumOf {
        if (it.type == "INPUT") it.amount else -it.amount
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 1. HEADER TITLE
        Text(
            text = "Liquid History",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 2. SUMMARY ROW (Today's Summary)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Todays Liquid",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$todaysTotal ml",
                style = MaterialTheme.typography.titleLarge,
                color = if (todaysTotal >= 0) Color(0xFF388E3C) else Color.Red,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 3. TABLE DATA (Taking weighted space)
        Column(modifier = Modifier.weight(1f)) {
            // Table Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray.copy(alpha = 0.4f))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Date", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
                Text(text = "Type", modifier = Modifier.weight(1.5f), fontWeight = FontWeight.Bold)
                Text(text = "Amount", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(48.dp)) // Space for delete icon
            }

            // Scrollable List
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(records) { record ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val date = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
                            .format(Date(record.timestamp))

                        Text(text = date, modifier = Modifier.weight(2f))
                        Text(text = record.type, modifier = Modifier.weight(1.5f))
                        Text(
                            text = "${record.amount} ml",
                            modifier = Modifier.weight(1f),
                            color = if (record.type == "OUTPUT") Color.Red else Color(0xFF388E3C)
                        )

                        IconButton(
                            onClick = {
                                scope.launch {
                                    db.liquidDao().delete(record)
                                }
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete record",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                }
            }
        }

        // 4. ACTION BUTTONS ROW
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 96.dp, start = 8.dp, end = 8.dp, top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    context.startActivity(intent)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Go Back")
            }

            Button(
                onClick = {
                    scope.launch {
                        db.liquidDao().deleteAll()
                    }
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Delete All")
            }
        }
    }
}
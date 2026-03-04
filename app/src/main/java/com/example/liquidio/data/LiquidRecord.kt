package com.example.liquidio.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liquid_records")
data class LiquidRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Int,          // The ml value from your knob
    val type: String,         // "INPUT" or "OUTPUT"
    val timestamp: Long = System.currentTimeMillis()
)
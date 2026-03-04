package com.example.liquidio.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LiquidDao {
    @Insert
    suspend fun insertRecord(record: LiquidRecord)

    @Query("SELECT * FROM liquid_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<LiquidRecord>>

    @Query("DELETE FROM liquid_records")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(record: LiquidRecord)
}

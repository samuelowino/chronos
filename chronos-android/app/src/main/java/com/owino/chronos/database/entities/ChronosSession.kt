package com.owino.chronos.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CHRONOS_SESSION")
data class ChronosSession(
    @PrimaryKey val uuid: String,
    @ColumnInfo(name = "SESSION_START") val sessionStart: String,
    @ColumnInfo(name = "SESSION_END") val sessionEnd: String,
    @ColumnInfo(name = "TARGET_HOURS") val targetHours: Int,
    @ColumnInfo(name = "COMPLETED_HOURS") val completedHours: Int)
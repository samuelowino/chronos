package com.owino.chronos

import com.owino.chronos.database.entities.ChronosSession
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DataDump {
    fun createChronosSession(): ChronosSession {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val currentDate: String = LocalDateTime.now().format(formatter)
        return ChronosSession(UUID.randomUUID().toString(), currentDate, currentDate,19,4)
    }
}
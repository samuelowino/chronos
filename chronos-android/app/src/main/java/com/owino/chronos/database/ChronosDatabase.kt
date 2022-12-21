package com.owino.chronos.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.owino.chronos.database.dao.ChronosSessionDao
import com.owino.chronos.database.entities.ChronosSession

@Database(entities = [ChronosSession::class], version = 1)
abstract class ChronosDatabase: RoomDatabase() {
    abstract fun sessionDao(): ChronosSessionDao
}
package com.owino.chronos.persistence_test

import androidx.room.Database
import androidx.room.RoomDatabase
import com.owino.chronos.database.dao.ChronosSessionDao

@Database(entities = [ChronosSessionDao::class], version = 1)
abstract class TestDatabase: RoomDatabase() {
    abstract fun chronosSessionDao(): ChronosSessionDao
}
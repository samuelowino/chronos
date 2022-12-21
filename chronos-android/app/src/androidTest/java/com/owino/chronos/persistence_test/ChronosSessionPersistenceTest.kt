package com.owino.chronos.persistence_test

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.owino.chronos.database.dao.ChronosSessionDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ChronosSessionPersistenceTest {
    private lateinit var database: TestDatabase
    private lateinit var chronosSessionDao: ChronosSessionDao

    @Before
    fun createDatabase(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context,TestDatabase::class.java)
            .build()
        chronosSessionDao = database.chronosSessionDao()
    }

    @Test
    fun shouldCreateChronosSessionDaoTest(){
        assert(chronosSessionDao != null)
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase(){
        database.close()
    }
}
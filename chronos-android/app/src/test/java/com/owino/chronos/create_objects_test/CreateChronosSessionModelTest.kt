package com.owino.chronos.create_objects_test

import com.owino.chronos.database.entities.ChronosSession
import org.junit.Test
import java.util.Date
import java.util.UUID

class CreateChronosSessionModelTest {
    @Test
    fun shouldCreateChronosSessionModelTest(){
        val session: ChronosSession = ChronosSession(UUID.randomUUID().toString(), Date(), Date(),19,4)
    }
}
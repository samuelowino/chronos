package com.owino.chronos.create_objects_test

import com.owino.chronos.database.entities.ChronosSession
import com.owino.chronos.utils.Constants
import org.assertj.core.api.Assertions
import org.junit.Test
import java.time.LocalDate
import java.util.*

class CreateChronosSessionModelTest {
    @Test
    fun shouldCreateChronosSessionModelTest(){
        val startSessionDate = LocalDate.of(2022,12,27).format(Constants.dateTimeFormatter)
        val endSessionDate = LocalDate.of(2022,12,28).format(Constants.dateTimeFormatter)
        val sessionUuid = UUID.randomUUID().toString()
        val targetHours = 19
        val completedHours = 4

        val session = ChronosSession(sessionUuid, startSessionDate, endSessionDate,19,4)

        Assertions.assertThat(session).isNotNull
        Assertions.assertThat(session.uuid).isEqualTo(sessionUuid)
        Assertions.assertThat(session.sessionStart).isEqualTo(startSessionDate)
        Assertions.assertThat(session.sessionEnd).isEqualTo(endSessionDate)
        Assertions.assertThat(session.targetHours).isEqualTo(targetHours)
        Assertions.assertThat(session.completedHours).isEqualTo(completedHours)
    }
}
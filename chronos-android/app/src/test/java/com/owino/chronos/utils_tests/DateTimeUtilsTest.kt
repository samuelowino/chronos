package com.owino.chronos.utils_tests

import com.owino.chronos.utils.Constants
import org.assertj.core.api.Assertions
import org.junit.Test
import java.time.LocalDate

class DateTimeUtilsTest {

    @Test
    fun shouldConvertLocalDateToStringTest() {
        val localDate = LocalDate.of(2022, 11, 6)
        val formattedDate = localDate.format(Constants.dateTimeFormatter)
        val expectedDate = "Sun 2022 11, 06"

        Assertions.assertThat(formattedDate).isNotEmpty
        Assertions.assertThat(formattedDate).isNotBlank
        Assertions.assertThat(formattedDate).isEqualTo(expectedDate)
    }
}
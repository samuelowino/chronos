package com.owino.chronos.create_objects_test

import com.owino.chronos.utils.Constants
import org.assertj.core.api.Assertions
import org.junit.Test

class CreateConstantsTest {

    @Test
    fun shouldCreateConstantsTest(){
        var constant = Constants
        Assertions.assertThat(constant.DATE_FORMAT_PATTERN).isEqualTo("EE yyyy MM, dd")
    }
}
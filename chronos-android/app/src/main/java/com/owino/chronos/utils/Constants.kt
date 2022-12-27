package com.owino.chronos.utils

import java.time.format.DateTimeFormatter

object Constants {
    val DATE_FORMAT_PATTERN = "EE yyyy MM, dd"
    var dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)
}
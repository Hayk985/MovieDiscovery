package com.moviediscovery.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class DateUtilsTest {

    @Test
    fun `passing incorrect format date returns the same date`() {
        val date = "10 12 2022"
        val result = DateUtils.formatDate(date)
        assertEquals("10 12 2022", result)
    }

    @Test
    fun `passing the correct date returns formatted date`() {
        val date = "2022-12-10"
        val result = DateUtils.formatDate(date)
        assertEquals("10 \nDec \n2022", result)
    }
}
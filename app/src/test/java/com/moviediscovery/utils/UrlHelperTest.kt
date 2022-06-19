package com.moviediscovery.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class UrlHelperTest {

    @Test
    fun `passing null poster url returns null`() {
        val result = UrlHelper.getPosterUrl(null)
        assertEquals(null, result)
    }

    @Test
    fun `passing empty poster url returns null`() {
        val result = UrlHelper.getPosterUrl(" ")
        assertEquals(null, result)
    }

    @Test
    fun `passing valid poster url returns complete url`() {
        val result = UrlHelper.getPosterUrl("/fOy2Jurz9k6RnJnMUMRDAgBwru2.jpg")
        assertEquals(
            "https://image.tmdb.org/t/p/w500/fOy2Jurz9k6RnJnMUMRDAgBwru2.jpg",
            result
        )
    }

    @Test
    fun `passing null backdrop url returns null`() {
        val result = UrlHelper.getBackDropUrl(null)
        assertEquals(null, result)
    }

    @Test
    fun `passing empty backdrop url returns null`() {
        val result = UrlHelper.getBackDropUrl(" ")
        assertEquals(null, result)
    }

    @Test
    fun `passing valid backdrop url returns complete url`() {
        val result = UrlHelper.getBackDropUrl("/fOy2Jurz9k6RnJnMUMRDAgBwru2.jpg")
        assertEquals(
            "https://image.tmdb.org/t/p/w1280/fOy2Jurz9k6RnJnMUMRDAgBwru2.jpg",
            result
        )
    }
}